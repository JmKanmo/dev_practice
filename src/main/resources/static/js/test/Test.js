var orderColumn = ["GRP_CD", "GRP_NM", "USE_YN", "DEL_YN", "CRT_DT"];
var dataTables = null;

function getJsonSampleData() {
    let jsonObject = {
        draw: 1,
        recordsTotal: 15,
        recordsFiltered: 15,
        data: []
    };

    for (let i = 1; i <= 1000; i++) {
        let dataObject = {
            grpCd: `TEST_GROUP${i}`,
            grpNm: `테스트 그룹${i}`,
            useYn: "N",
            delYn: "N",
            crtDt: `2021-03-19 15:36:3${i}`,
            crtBy: 0,
        };
        jsonObject.data.push(dataObject);
    }
    return jsonObject;
}

function dataTablesClientSide(testData) {
    $(document).ready(function () {
        $('#table').DataTable({
            "processing": true,
            "data": testData, // 클라이언트 사이드에서 처리할 데이터
            "columns": [
                {"data": "grpCd"},
                {"data": "grpNm"},
                {"data": "useYn"},
                {"data": "delYn"},
                {"data": "crtDt"}
            ]
        });
    });

}

function dataTablesServerSide() {
    dataTables = $('#table').DataTable({
        "paging": true,
        "searching": false,
        "info": true,
        "autoWidth": false,
        "responsive": true,
        "lengthChange": true,
        "lengthMenu": [10, 20, 50],
        "ordering": true,
        "columns": [
            {"data": "delYn"},
            {"data": "grpNm"},
            {"data": "grpCd"},
            {"data": "useYn"},
            {"data": "crtDt"}
        ],
        "processing": true,
        "serverSide": true,
        "ajax": {
            url: "/test/json",
            method: 'POST',
            data: function (data) {
                data.keyword = !document.getElementById("searchKeyword").value ? '' : document.getElementById("searchKeyword").value;
                return data;
            }
        }
    });
}

document.addEventListener("DOMContentLoaded", () => {
    // dataTablesClientSide(getJsonSampleData()["data"]);
    dataTablesServerSide();

    document.getElementById("searchKeywordButton").addEventListener("click", evt => {
        dataTables.ajax.reload();
    });
});