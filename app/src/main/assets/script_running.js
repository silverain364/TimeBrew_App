//차라리 Drop를 시킬까
document.getElementById("changeMode-btn").remove();
document.getElementById("tool-container").remove();
document.getElementById("left-sideTab").remove();

const tables = [...document.querySelectorAll(".draggable")];

tables.forEach(table => {
    table.addEventListener("mousedown", (e) => {
        AndroidInterface.selectTable(Number(table.dataset.tableId));
    })
})

function getTableNumbers(){
    console.log(tables.map(table => table.dataset.tableId));
    return tables.map(table => table.dataset.tableId);
}

