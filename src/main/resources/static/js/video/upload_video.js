class UploadVideoController {
    constructor() {
        this.videoUploadFileInput = document.getElementById("video_upload_file");
        this.uploadStatusShowBox = document.getElementById("upload_status_show_box");
        this.toggleButton = document.getElementById("toggle-btn");
        this.convertedFiles = [];
        this.chunkSize = 10485760; // 10MB
        // tus server config
        this.tusUploadProtocol = 'http'; // http | https
        this.tusServerAddress = "192.168.35.98";
        this.tusSaveDirectory = "save";
        this.tusUploadType = "videos";
    }

    initUploadVideoController() {
        this.initEventHandler();
    }

    initEventHandler() {
        this.videoUploadFileInput.addEventListener("change", evt => {
            this.uploadStatusShowBox.innerHTML = ``;
            const files = evt.target.files;

            // init convertedFiles
            Object.keys(files).forEach(k => {
                this.convertedFiles = [
                    ...this.convertedFiles,
                    {id: URL.createObjectURL(files[k]), file: files[k]}
                ];
            });

            this.convertedFiles.map((i, key) => {
                key++

                const file = i.file;

                if (this.isVideoFile(file.name) === false) {
                    console.log("비디오 파일 확장자가 아닙니다.");
                    return;
                }
                const chunkSize = parseInt(this.chunkSize, 10);
                const formattedDate = this.generateDate();
                const savePath = this.deleteSpace(this.generateSavePath(formattedDate));
                const vodName = this.deleteSpace(this.generateVodName(file.name));
                const extension = this.deleteSpace(this.getFileExtension(file.name));

                this.uploadStatusShowBox.innerHTML += `
                                <div class="file-data" key=${key}>
                                    <!-- <img alt="what" src=${i.id} width="150" /> -->
                                    <div>파일명: ${i.file.name}</div>
                                    <div>파일 타입: ${i.file.type}</div>
                                    <div>파일 크기: ${i.file.size} byte</div>
                                    <div class="flex-grow-1">
                                        <div class="progress pr_${key}">
                                            <div class="progress-bar_${key} progress-bar-striped bg-success" role="progressbar"></div>
                                        </div>
                                        <div class="upload-text-progress" id="js-upload-text-progress_${key}"></div>
                                    </div>
                                </div> `;

                const upload = new tus.Upload(file, {
                    endpoint: `/tus/upload/video`,
                    chunkSize,
                    retryDelays: [0, 1000, 3000, 5000],
                    metadata: {
                        filename: file.name,
                        filetype: file.type,
                        date: formattedDate,
                        // savePath: savePath,
                        vodName: vodName
                    },
                    onError: function (error) {
                        console.log("Failed because: " + error);
                    },
                    onProgress: function (bytesUploaded, bytesTotal) {
                        const percentage = ((bytesUploaded / bytesTotal) * 100).toFixed(2);

                        $('.progress-bar_' + key).css('width', percentage + '%');

                        if (percentage < 100.00) {
                            $('#js-upload-text-progress_' + key).html(`Uploaded ${this.formatBytes(bytesUploaded)} of ${this.formatBytes(bytesTotal)} (${percentage}%)`);
                        } else {
                            $('#js-upload-text-progress_' + key).html(`Uploaded ${this.formatBytes(bytesUploaded)} of ${this.formatBytes(bytesTotal)} (${percentage}%) 파일 저장 중`);
                        }
                    },
                    onSuccess: function () {
                        const videoSrc = savePath + '/' + vodName;
                        $('#js-upload-text-progress_' + key).html("파일 저장 완료");
                        document.getElementById("video_result_box").innerHTML += `
                        <video height="180" width="288" controls="">
                            <source src=${videoSrc} type="video/mp4">
                            이 문장은 여러분의 브라우저가 video 태그를 지원하지 않을 때 화면에 표시됩니다!
                        </video>`
                    },
                    onAfterResponse: function (req, res) {
                        console.log(req + ", " + res);
                    },
                    /**
                     * Turn a byte number into a human readable format.
                     * Taken from https://stackoverflow.com/a/18650828
                     */
                    formatBytes: function (bytes, decimals = 2) {
                        if (bytes === 0) {
                            return '0 Bytes';
                        }
                        const k = 1024
                        const dm = decimals < 0 ? 0 : decimals
                        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB']
                        const i = Math.floor(Math.log(bytes) / Math.log(k))
                        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
                    }
                });

                this.toggleButton.addEventListener('click', (e) => {
                    this.reset();
                    // Check if there are any previous uploads to continue.
                    upload.findPreviousUploads().then(function (previousUploads) {
                        // Found previous uploads so we select the first one.
                        if (previousUploads.length) {
                            upload.resumeFromPreviousUpload(previousUploads[0]);
                        }
                        // Start the upload
                        upload.start();
                    })
                });
            });
        });
    }

    reset() {
        this.convertedFiles.map((i, key) => {
            $('.progress-bar_' + key).css('width', '0%');
            $('#js-upload-text-progress_' + key).html('');
        });
    }

    generateDate() {
        const currentDate = new Date();
        const year = currentDate.getFullYear();
        const month = String(currentDate.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 1을 더하고 두 자리로 만듭니다.
        const day = String(currentDate.getDate()).padStart(2, '0');
        const formattedDate = `${year}-${month}-${day}`;
        return formattedDate;
    }

    generateSavePath(formattedDate) {
        return `${this.tusUploadProtocol}://${this.tusServerAddress}/${this.tusUploadType}/${this.tusSaveDirectory}/${formattedDate}`;
    }

    generateVodName(fileName) {
        return `${new Date().getTime()}_${this.generateString(30)}_${fileName}`;
    }

    generateString(length) {
        // program to generate random strings
        // declare all characters
        const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let result = '';
        const charactersLength = characters.length;
        for (let i = 0; i < length; i++) {
            result += characters.charAt(Math.floor(Math.random() * charactersLength));
        }
        return result;
    }

    getFileExtension(filename) {
        return filename.slice(((filename.lastIndexOf(".") - 1 >>> 0) + 2)).toLowerCase();
    }

    isVideoFile(filename) {
        const allowedExtensions = ['mp4', 'avi', 'mkv', 'wmv', 'flv', 'mov', 'webm', '3gp'];

        // 파일 이름이 null이거나 빈 문자열인 경우 유효하지 않음
        if (!filename) {
            return false;
        }

        // 파일 이름에서 마지막 점('.') 이후의 문자열 추출
        const fileExtension = filename.slice(((filename.lastIndexOf(".") - 1 >>> 0) + 2)).toLowerCase();

        // 허용 가능한 확장자 목록과 비교하여 유효성 검사
        return allowedExtensions.includes(fileExtension);
    }

    deleteSpace(str) {
        if (!str) {
            return str;
        }
        return str.split('').filter(char => char !== ' ').join('');
    }
}

// Execute all functions
document.addEventListener("DOMContentLoaded", () => {
    const uploadVideoController = new UploadVideoController();
    uploadVideoController.initUploadVideoController();
});