/**
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
    const decreaseBtn = document.getElementById("decrease-btn");
    const increaseBtn = document.getElementById("increase-btn");
    const peopleCountElem = document.getElementById("people-count");
    const peopleInput = document.getElementById("people-input");

    let count = 1;   // 初期値
    const minCount = 1;
    const maxCount = 10;

    function updateDisplay() {
        peopleCountElem.textContent = count;
        peopleInput.value = count;
    }

    decreaseBtn.addEventListener("click", () => {
        if (count > minCount) {
            count--;
            updateDisplay();
        }
    });

    increaseBtn.addEventListener("click", () => {
        if (count < maxCount) {
            count++;
            updateDisplay();
        }
    });

    // 初期表示
    updateDisplay();
});