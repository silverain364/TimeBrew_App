<!-- 삭제로직2 -->
<!-- 부분 성공 : item 요소는 되는데 , draggable 요소는 안됨 -->
<!-- draggable 는 waste_basket 에서 dragover로 처리해야할 것 같다.  -->

//div가 중첩되었는지 확인하는 메서드
function isOverlapping(x, y) {
    const rect1 = x, rect2 = y;

    return !(
        rect1.top > rect2.bottom ||
        rect1.right < rect2.left ||
        rect1.bottom < rect2.top ||
        rect1.left > rect2.right
    );
}

let inWasteBasket = false;

//쓰레기통 안으로 왔을 때 판단해서 효과를 주는 함수
function wasteIn(remove, position = remove.getBoundingClientRect()) {
    if (isOverlapping(position, wasteBasket.getBoundingClientRect())) { //쓰레기통 안으로 들어왔다면
        if (inWasteBasket) return; //클래스가 계속 추가되는 경우를 방지한다.

        //css 옵션을 준다.
        remove.classList.add("waste-in-object");
        wasteBasket.classList.add("waste-in");
        inWasteBasket = true;
    } else { //겹치지 않았다면 --> 쓰레기통 밖으로 나왔다면
        //추가했던 css 옵션을 제거한다.
        remove.classList.remove("waste-in-object");
        wasteBasket.classList.remove("waste-in");
        inWasteBasket = false;
    }
}

function setRemoveItemEvent(item) {
    //마우스 오버를 할 때 쓰레기통 안에 객체가 있다면 색 변경 및 플래그 설정
    item.addEventListener("mousemove", () => wasteIn(item));
    item.addEventListener("touchmove", () => wasteIn(item));

    //마우스가 끝났을 때 발생하는 이벤트
    item.addEventListener("mouseup", () => checkWasteOverlapping(item));
    item.addEventListener("touchend", () => checkWasteOverlapping(item));
}

function checkWasteOverlapping(item) {
    if (isOverlapping(wasteBasket.getBoundingClientRect(),
        item.getBoundingClientRect())) item.remove(); //마우스를 놓은 곳이 객체 안에 있다면 그 요소를 사젝하겠다.
    wasteBasket.classList.remove("waste-in");
}

function setRemoveDragEvent(drag) {
    drag.addEventListener("drag", e => {
        wasteIn(drag, new DOMRect(e.clientX, e.clientY, 1, 1))
    });

    drag.addEventListener("touchmove", e => {
        wasteIn(drag, new DOMRect(e.touches[0].clientX, e.touches[0].clientY, 1, 1))
    });

    drag.addEventListener("dragend", e => {
        if (isOverlapping(wasteBasket.getBoundingClientRect(),
            new DOMRect(e.clientX, e.clientY, 1, 1))) drag.remove();
        wasteBasket.classList.remove("waste-in"); //종료 시점에 결과에 상관없이 휴지통을 원래대로 한다.
    })

    drag.addEventListener("touchend", e => {
        e.stopPropagation();
        if (isOverlapping(wasteBasket.getBoundingClientRect(),
            new DOMRect(e.changedTouches[0].pageX, e.changedTouches[0].pageY, 1, 1))) drag.remove();
        wasteBasket.classList.remove("waste-in"); //종료 시점에 결과에 상관없이 휴지통을 원래대로 한다.
    })
}

//자유롭게 움직일 수 있는 요소들
const removableItemList = document.querySelectorAll(".item");

//붙여지는 요소들
const removableDragList = document.querySelectorAll(".draggable, .container-draggable")
const wasteBasket = document.getElementById("waste-container");


removableItemList.forEach(removable => {
    setRemoveItemEvent(removable);
})

removableDragList.forEach(removable => {
    setRemoveDragEvent(removable);
})



let resourceDragend; //item이면서 resource가 drag하고 있는 객체
let resources = document.querySelectorAll(".resource"); //모든 자원을 가져온다.

resources.forEach(resource => {
    if (resource.classList.contains("item")) {
        resource.addEventListener("drag", e => {
            resourceDragend = resource;
        })

        resource.addEventListener("dragend", e => {
            //resource item인 drop 된 경우
            resourceItemDrop(e);
        });

        resource.addEventListener("touchstart", e => {
            resourceDragend = resource;
            touchedFlag = true;

            //잔상 처리
            afterImage = resource.cloneNode(true);

            afterImage.removeAttribute("id");
            afterImage.classList.add("afterImage")
            document.getElementById("main-container").appendChild(afterImage);
        })

        resource.addEventListener("touchend", e => {
            afterImage.remove();
            touchedFlag = false;
            resourceItemDrop(e);
        });
    }
})


let
    isDrag = false;
//resource를 가져온다.
//let resourceTableContainer = document.q(".resource.draggable").values();

const leftSideTab = document.getElementById('left-sideTab');

//붙이는 요소들 가져온다.
const draggables = document.querySelectorAll(".draggable");
draggables.forEach(container => {
    setDraggableEvent(container);
})

//모든 1단계 컨테이너를 가져온다.
const draggableContainers = document.querySelectorAll(".container-draggable");
draggableContainers.forEach(container => {
    setDraggableEvent(container);

    if (!container.classList.contains("resource")) {
        setContainerAttachEvent(container, "draggable");//이 컨테이에 담을 수 있는 요소
    }
})

//모든 2단계 컨테이너를 가져온다.
const lineContainers = document.querySelectorAll(".container-line");
lineContainers.forEach(container => {
    if (!container.classList.contains("resource")) {
        setContainerAttachEvent(container, "container-draggable"); //이 컨테이에 담을 수 있는 요소
    }
})

//테이블 id를 변경하는 로직
let focusSetNumberTable;
const inputTableId = document.getElementById("input-tableId");
const tableSizeBtns = document.getElementsByName("tableSize");

//텍스트 사용자에 내용이 변경되는 경우
inputTableId.addEventListener("change", e => {
    focusSetNumberTable.dataset.tableId = e.target.value;
    tabletNumberTextChange(focusSetNumberTable);
})

//사이즈가 변하는 경우
tableSizeBtns.forEach(btn => {
    btn.addEventListener("change", e => {
        focusSetNumberTable.classList.remove("table-" + focusSetNumberTable.dataset.tableSize);
        focusSetNumberTable.classList.add("table-" + btn.dataset.tableSize);
        focusSetNumberTable.dataset.tableSize = btn.dataset.tableSize;
    });
})

//클릭해서 테이블 번호 저장
function setNumberTable(table) {
    if (focusSetNumberTable !== undefined)
        focusSetNumberTable.classList.toggle("focus"); //원래 선택된 객체 선택 해제

    if (focusSetNumberTable === table) { //기존에 선택한 테이블 다시 선택하면 취소
        focusSetNumberTable = undefined;
        leftSideTab.classList.remove("open");
        return;
    }

    focusSetNumberTable = table;
    focusSetNumberTable.classList.toggle("focus");

    tableSizeBtns.forEach(btn => {
        if (btn.dataset.tableSize === table.dataset.tableSize)
            btn.checked = true;
    })

    inputTableId.value = Number(table.dataset.tableId);
    leftSideTab.classList.add("open");
}

let touchedFlag = false;

//드레그
function setDraggableEvent(draggable) {
    //드레그가 시작된 경우 상태를 변경
    draggable.addEventListener("dragstart", e => setStartDragEvent(draggable, e));
    draggable.addEventListener("touchstart", e => {
        setStartDragEvent(draggable, e);
    });

    draggable.addEventListener("touchmove", e => {
        if (touchedFlag) return;
        touchedFlag = true;

        //잔상 처리
        afterImage = draggable.cloneNode(true);
        console.log(`afterImage ${afterImage}`)

        afterImage.removeAttribute("id");
        afterImage.classList.add("afterImage")
        document.getElementById("main-container").appendChild(afterImage);
    })

    //드레그가 끝나면 상태를 종료한다.
    draggable.addEventListener("dragend", e => setEndDragEvent(draggable, e));
    draggable.addEventListener("touchend", e => {
        setEndDragEvent(draggable, e);
        afterImage.remove();
        touchedFlag = false;
    });
}


document.addEventListener("touchmove", e => {
    if (afterImage === undefined) return;

    setTranslate(e.touches[0].clientX - afterImage.getBoundingClientRect().width / 2
        , e.touches[0].clientY - afterImage.getBoundingClientRect().height / 2
        , afterImage);

    const touchElement = document.elementFromPoint(e.touches[0].clientX, e.touches[0].clientY);

    if (touchElement.classList.contains("attachable")) {
        const isHorizontal = getComputedStyle(touchElement).flexDirection === "row";
        draggableInContainer(touchElement, getTargetType(touchElement), e, isHorizontal);
    }

})

function setStartDragEvent(draggable, event) {
    event.stopPropagation()
    isDrag = true;
    draggable.classList.add("dragging");
    console.log("drag start!")
}

function setEndDragEvent(draggable, event) {
    //event.stopPropagation(); // 종료되는건 전파되어도 되지 않을까?
    isDrag = false;
    draggable.classList.remove("dragging");
    console.log("drag end!")
}

function getTargetType(container) {
    let targetType;

    if (container.classList.contains("container-draggable"))
        targetType = "draggable";
    else if (container.classList.contains("container-line"))
        targetType = "container-draggable";


    return targetType;
}


function setContainerAttachEvent(container, target) {
    let targetType = target === undefined ? getTargetType(container) : target;

    const isHorizontal = getComputedStyle(container).flexDirection === "row";

    //마우스가 container 영역으로 이동했다면
    container.addEventListener("dragover", e => { //드레그 오버 상태면 계속 발생하는 이벤트
        draggableInContainer(container, targetType, e, isHorizontal);
    });
    // container.addEventListener("touchmove", e=> {
    //     draggableInContainer(container, targetType, e, isHorizontal);
    // })
}

function draggableInContainer(container, targetType, event, isHorizontal) {
    //모든 컨테이너들에 대해서 이벤트를 추가한다.
    let draggable = document.querySelector(".dragging"); //선택된 항목을 가져온다.
    console.log("dragover! : " + draggable);

    if (draggable === null) return;
    if (!draggable.classList.contains(targetType)) return //container에 넣을 수 있는 항목인지 확인한다.


    event.preventDefault(); //어떤 의미인지 잘 모르겠군... 특정 이벤트를 중시키는 것 같긴한데...
    //event.stopPropagation(); //부모 객체로 예외 전파 방지 --> 위에서 예외처리 하기 때문에 없어도 괜찮을 듯

    const afterElement = getDragAfterElement(container, event, targetType, isHorizontal); //마우스 좌표에서 가장 오른쪽에 있는 항목을 가져온다.

    if (draggable.classList.contains("resource")) { //삭제되지 않는 요소라고 하면
        let x = draggable.cloneNode(true);

        //이벤트 바인딩
        setDraggableEvent(x);
        setRemoveDragEvent(x);
        if (draggable.classList.contains("container-draggable")) {
            setContainerAttachEvent(draggable, "draggable");
            draggable.innerText = draggable.innerText.trim().substring(0, 1);
        }

        x.classList.remove("dragging");
        draggable.classList.remove("resource");
        draggable.removeAttribute("id");
        console.log("id : " + x.id.toString())

        draggable.after(x);

        //테이블 번호 최신화
        if (x.classList.contains("draggable")) {
            draggable.addEventListener("mousedown", e => {
                e.stopPropagation();
                setNumberTable(draggable);
            });

            // draggable.addEventListener("touchstart", e => {
            //     setNumberTable(draggable);
            // })
            const tableNumber = (Number(draggable.dataset.tableId) + 1).toString();
            tableNumberTable(tableNumber);
        }
    }

    if (afterElement === undefined) { //오른쪽에 항목이 없다면
        container.appendChild(draggable); //가장 뒤에 추가해준다.
    } else {
        container.insertBefore(draggable, afterElement);
    }
}


function tableNumberTable(tableNumber) {
    const table1 = document.getElementById("table-1");
    const table4 = document.getElementById("table-4");

    table1.dataset.tableId = tableNumber;
    table4.dataset.tableId = tableNumber;

    tabletNumberTextChange(table1);
    tabletNumberTextChange(table4);
}

function tabletNumberTextChange(table) {
    table.innerHTML = `<h2>T ${table.dataset.tableId}<br>00:00</h2>`;
}

//컨테이터 내에 선택되지 않은 항목들 중에 마우스 왼쩍에 있는 항목을 리턴하는 함수
function getDragAfterElement(container, e, targetType, isHorizontal) {
    const draggableElements = [
        //... -> 배열로 가져온다.
        ...container.querySelectorAll(`.${targetType}:not(.dragging)`), // 컨테이너 목록중에 targetType 요소중 dragging 이 아닌 것을 가져온다.
    ];

    //선택되지 않은 항목들의 마우스를 기준으로 가장 오른쪽에 있는 항목을 가져온다.
    return draggableElements.reduce( //콜백 메서드를 배열 만큼 실행하고 하나의 값을 반환하는 함수
        (closest, child) => {
            const box = child.getBoundingClientRect(); //도형 정보를 가져온다.
            //마우스 x좌표 - 박스 왼쪽 위치 - 박스 넓이 / 2
            //affset 값이 음수라면 마우스 기준으로 박스 중심이 오른쪽에 있고 양수라면 왼쪽에 있다.(박스를 기준으로 마우스는 왼쪽, 오른쪽에 있다.)
            let offset;
            if (isHorizontal)
                offset = (e.type === "dragover" ? e.clientX : e.touches[0].clientX)
                    - (box.left + box.width / 2);
            else
                offset = (e.type === "dragover" ? e.clientY : e.touches[0].clientY)
                    - (box.top + box.height / 2);

            //마우스 좌표가 도형보다 왼쪽에 있고 전 항목보다 0에 가깝다면 offset값과 항목을 갱신
            if (offset < 0 && offset > closest.offset) {
                return {offset: offset, element: child};
            } else { //아니면 원래 값 다시 갱신
                return closest;
            }
        },
        {offset: Number.NEGATIVE_INFINITY}
    ).element;
}


<!-- 자유롭게 움직이는 로직 -->

//item를 자겨온다.
var dragItem = document.querySelectorAll(".item");
//container를 가져온다.
var container = document.querySelector("#workspace");

var active = false;
var target
var currentX;
var currentY;
var initialX;
var initialY;
//translate3d에 있는 값을 반영해야하기 때문에 Offset 값을 저장해야 된다.
var itemOffPositionMap = new Map();
dragItem.forEach(item => {
    if (item.classList.contains("resource"))
        return;
    itemOffPositionMap.set(item, {x: 0, y: 0});
})

//터치 이벤트에 대한 처리
container.addEventListener("touchstart", dragStart, false);
container.addEventListener("touchend", dragEnd, false);
container.addEventListener("touchmove", drag, false);
//마우스 클릭에 대한 이벤트 처리
container.addEventListener("mousedown", dragStart, false);
container.addEventListener("mouseup", dragEnd, false);
container.addEventListener("mousemove", drag, false);

":"


function resourceItemDrop(e) {
    e.preventDefault();
    if (resourceDragend === undefined) return;
    let clone = resourceDragend.cloneNode(true); //깊은 복사
    let isTouch = e.type === "touchend";

    //item에 맞는 속성으로 초기화
    clone.classList.remove("resource");
    clone.draggable = false;
    clone.style.position = "absolute"
    container.appendChild(clone);

    //중심을 기준으로 좌표 구하기 및 map에 등록
    console.log("isTouch : " + isTouch);
    let initPosition = {
        x: (isTouch ? e.changedTouches[0].pageX : e.clientX) - container.getBoundingClientRect().width / 2,
        y: (isTouch ? e.changedTouches[0].pageY : e.clientY) - container.getBoundingClientRect().height / 2
    };


    itemOffPositionMap.set(clone, initPosition);

    //구한 좌표로 이동 및
    setTranslate(initPosition.x, initPosition.y, clone);

    //텍스트 변경
    clone.innerHTML = `<h3 style="margin: 0px">${clone.innerText.trim().substring(0, 1)}</h3>`;


    //이벤트 바인드
    setContainerAttachEvent(clone);
    setRemoveItemEvent(clone);

    resourceDragend = undefined;
}


container.addEventListener("dragover", e => e.preventDefault());

function dragStart(e) {
    if (isDrag) return;
    if (active || e.target === container) return
    if (e.target.draggable == true) return;
    target = e.target

    while (target.parentNode !== container) {
        target = target.parentNode;
    }

    if (itemOffPositionMap.has(target)) { //dragItem 이 클린된 경우
        let offset = itemOffPositionMap.get(target);

        if (e.type === "touchstart") {
            initialX = e.touches[0].clientX - offset.x
            initialY = e.touches[0].clientY - offset.y
        } else {
            initialX = e.clientX - offset.x;
            initialY = e.clientY - offset.y;
        }

        active = true;
    }
}

function dragEnd() {
    if (active) { //dragItem 이 클린된 경우
        var offset = itemOffPositionMap.get(target)
        offset.x = currentX
        offset.y = currentY
    }
    active = false;
}

function drag(e) {
    if (active) { //dragItem 선택된 경우
        e.preventDefault();

        if (e.type === "touchmove") { //터치한 상태로 이동한 경우
            currentX = e.touches[0].clientX - initialX;
            currentY = e.touches[0].clientY - initialY;
        } else { //마우스로 이동하는 경우
            currentX = e.clientX - initialX;
            currentY = e.clientY - initialY;
        }

        setTranslate(currentX, currentY, target);
    }
}

let scale = 1;

function setTranslate(xPos, yPos, el) {
    el.style.transform = "translate3d(" + xPos + "px, " + yPos + "px, 0) scale(" + scale + ")";
}


<!-- 줌인 아웃 -->

<!-- 왼쪽 화면 접기 -->

function zoomIn() {
    scale += 0.1;
    itemOffPositionMap.values()
    itemOffPositionMap.forEach((value, key) => {
        setTranslate(value.x, value.y, key);
    })

}

function zoomOut() {
    if (scale <= 0.1) return

    scale -= 0.1;
    itemOffPositionMap.forEach((value, key) => {
        setTranslate(value.x, value.y, key);
    })
}

document.getElementById("left-backBtn").addEventListener("mousedown", e => {
    if (focusSetNumberTable !== undefined) {
        focusSetNumberTable.classList.toggle("focus");
        focusSetNumberTable = undefined;
    }
    leftSideTab.classList.toggle("open");
});


const toolContainer = document.getElementById("tool-container");
document.getElementById("tool-container-toggleBtn").addEventListener("mousedown", e => {
    toolContainer.classList.toggle("close");
    e.target.textContent = e.target.textContent == ">" ? "<" : ">";
})


let editMode = true;
const changeModeBtn = document.getElementById("changeMode-btn");
changeModeBtn.addEventListener("mousedown", e => {
   if(editMode){
       changeModeBtn.innerText = "Edit Mode";
       document.getElementById("tool-container").style.display = "none";
       document.getElementById("left-sideTab").style.display = "none";
       document.getElementById("workspace").style.pointerEvents = "none";
   }else{
       changeModeBtn.innerText = "View Mode";
       document.getElementById("tool-container").style.display = "block";
       document.getElementById("left-sideTab").style.display = "block";
       document.getElementById("workspace").style.pointerEvents = "auto";

   }

   editMode = !editMode;
});