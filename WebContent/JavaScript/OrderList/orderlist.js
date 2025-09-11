/**
 * 
 */
//増減ボタン処理
function updateOrderState(menuId, menuQuantity, menuStock, _menuSubtotal, toppings) {
	const storedQuantity = sessionStorage.getItem(`menu_quantity_${menuId}`);
	const initialQuantity = storedQuantity !== null ? parseInt(storedQuantity) : menuQuantity;
	const orderstate = { count: initialQuantity };

	const incrementbtn = document.getElementById(`increment-${menuId}`);
	const decrementbtn = document.getElementById(`decrement-${menuId}`);
	const counter = document.getElementById(`counter-${menuId}`);
	const subtotal = document.getElementById(`subtotal-${menuId}`);

	const pricePerItem = parseFloat(subtotal?.dataset.price || 0); // ここで単価取得

	if (counter) counter.innerHTML = orderstate.count;
	if (subtotal) subtotal.innerHTML = pricePerItem * orderstate.count;

	if (incrementbtn && decrementbtn && counter && subtotal) {
		incrementbtn.replaceWith(incrementbtn.cloneNode(true));
		decrementbtn.replaceWith(decrementbtn.cloneNode(true));

		const newIncrementBtn = document.getElementById(`increment-${menuId}`);
		const newDecrementBtn = document.getElementById(`decrement-${menuId}`);

		newIncrementBtn.addEventListener('click', () => {
			const productElem=document.getElementById(`productId-${menuId}`);
			if(!productElem) return;
			
			const productId=productElem.value;
			const totalQuantity=getTotalQuantityForProduct(productId);
			
			// 商品の在庫チェック
			if(totalQuantity >= menuStock) return;
			if (totalQuantity < menuStock) {
				const quantityValue = ++orderstate.count;
				counter.innerHTML = quantityValue;
				const subtotalValue = pricePerItem * quantityValue;
				subtotal.innerHTML = subtotalValue;
				setQuantity(quantityValue, toppings, menuId);
				setPrice(subtotalValue, menuId);
				updateButtonDisplay(menuId, orderstate.count);
				updateTotal();
			}
		});

		newDecrementBtn.addEventListener('click', (event) => {
			event.preventDefault();
			const mode = newDecrementBtn.getAttribute('data-mode');
			if (mode === 'delete') {
				showDeletePopup(menuId);
			} else {
				if (orderstate.count > 0) {
					const quantityValue = --orderstate.count;
					counter.innerHTML = quantityValue;
					const subtotalValue = pricePerItem * quantityValue;
					subtotal.innerHTML = subtotalValue;
					setQuantity(quantityValue, toppings, menuId);
					setPrice(subtotalValue, menuId);
					updateButtonDisplay(menuId, orderstate.count);
					updateTotal();
				}
			}
		});

		updateButtonDisplay(menuId, orderstate.count);
	}
}

// 同じ商品IDの合計数を取得
function getTotalQuantityForProduct(productId) {
  let total = 0;
  for (let key in sessionStorage) {
    if (key.startsWith("menu_quantity_")) {
      const orderId = key.replace("menu_quantity_", "");
      const qty = parseInt(sessionStorage.getItem(key));
      if (!isNaN(qty)) {
        const productElem = document.getElementById(`productId-${orderId}`);
        if (productElem && productElem.value == productId) {
          total += qty;
        }
      }
    }
  }
  return total;
}

// 同じトッピングIDの合計数を取得
function getTotalQuantityForTopping(toppingId) {
  let total = 0;
  Object.keys(sessionStorage).forEach(key => {
    if (key.startsWith("topping_quantity_")) {
      if (key.includes(`_${toppingId}`)) {
        const qty = parseInt(sessionStorage.getItem(key));
        if (!isNaN(qty)) total += qty;
      }
    }
  });
  return total;
}

//ゴミ箱ボタンとマイナスボタンの切替処理
function updateButtonDisplay(orderId, quantity) {
	const decrementBtn = document.getElementById(`decrement-${orderId}`);
	if (!decrementBtn) return;
	//中の子要素だけを書き換える
	decrementBtn.innerHTML = '';
	//menu_quantityが1のときはゴミ箱ボタンを表示
	if (quantity === 1) {
		const img = document.createElement('img');
		img.src = contextPath + '/image/dustbox.png';
		img.alt = 'ゴミ箱ボタン';
		img.classList.add('dustbox-img');
		decrementBtn.appendChild(img);
		//状態を属性に持たせる(削除)
		decrementBtn.setAttribute('data-mode', 'delete');
		//それ以外のときはマイナスボタンを表示
	} else {
		decrementBtn.textContent = '−';
		//状態を属性に持たせる(減少)
		decrementBtn.setAttribute('data-mode', 'decrement');
	}
}
//ポップアップ処理
function showDeletePopup(orderId) {
	const popupOverlay = document.getElementById('popup-overlay');
	const popupContent = document.getElementById('popup-content');
	const closePopupButton = document.getElementById('close-popup');
	const confirmButton = document.getElementById('confirm-button');
	const hiddenInput = document.getElementById('popup-order-id');
	//order_idをセット
	if (hiddenInput) hiddenInput.value = orderId;

	popupOverlay.classList.add('show');
	popupContent.classList.add('show');

	closePopupButton.onclick = () => {
		popupOverlay.classList.remove('show');
		popupContent.classList.remove('show');
	};

	confirmButton.onclick = () => {
		popupOverlay.classList.remove('show');
		popupContent.classList.remove('show');

		const counter = document.getElementById(`counter-${orderId}`);
		const subtotal = document.getElementById(`subtotal-${orderId}`);
		if (counter && subtotal) {
			counter.innerHTML = '0';
			subtotal.innerHTML = '0';
			sessionStorage.removeItem(`menu_quantity_${orderId}`);
			sessionStorage.removeItem(`menu_subtotal_${orderId}`);
			sessionStorage.removeItem(`topping_quantity_${menuId}_${topping.id}`);
			setPrice(0, orderId);
			updateButtonDisplay(orderId, 0);
			updateTotal();
		}
	};
}
//合計の更新処理
function updateTotal() {
	let total = 0;
	const subtotalElems = document.querySelectorAll('[id^="subtotal-"]');
	subtotalElems.forEach(elem => {
		const value = parseInt(elem.innerHTML);
		if (!isNaN(value)) total += value;
	});

	const totalElem = document.getElementById('total');
	if (totalElem) {
		totalElem.innerHTML = `合計:${total}円(税込)`;
	}
}
//小計の更新処理
function setPrice(price, menuId) {
	const priceField = document.getElementById(`priceField-${menuId}`);
	if (priceField) priceField.value = price;

	sessionStorage.setItem(`menu_subtotal_${menuId}`, price);
}
//個数の更新処理
function setQuantity(count, toppings, menuId) {
	const countField = document.getElementById(`countField-${menuId}`);
	if (countField) countField.value = count;

	toppings.forEach(topping => {
		const toppingcountField = document.getElementById(`toppingcountField-${menuId}-${topping.id}`);
		const quantity = topping.quantity * count;
		if (toppingcountField) {
			toppingcountField.value = quantity;
		}

		sessionStorage.setItem(`topping_quantity_${menuId}_${topping.id}`, quantity);
	});

	sessionStorage.setItem(`menu_quantity_${menuId}`, count);
}

//ページ読み込み初期化
window.addEventListener('DOMContentLoaded', () => {
	const allCounters = document.querySelectorAll('[id^="counter-"]');

	allCounters.forEach(counterElem => {
		const menuId = counterElem.id.replace('counter-', '');
		const subtotal = document.getElementById(`subtotal-${menuId}`);
		const pricePerItem = parseFloat(subtotal?.dataset.price || 0);

		let count = parseInt(sessionStorage.getItem(`menu_quantity_${menuId}`));
		if (isNaN(count)) {
			const countField = document.getElementById(`countField-${menuId}`);
			count = countField ? parseInt(countField.value) : 1;
		}

		if (!isNaN(count)) {
			// 個数を復元
			counterElem.innerHTML = count;

			const countField = document.getElementById(`countField-${menuId}`);
			if (countField) countField.value = count;

			// 小計を復元
			const subtotalValue = pricePerItem * count;
			if (subtotal) subtotal.innerHTML = subtotalValue;
			setPrice(subtotalValue, menuId);

			// トッピングの復元処理を追加
			const toppingFields = document.querySelectorAll(`[id^="toppingcountField-${menuId}-"]`);
			toppingFields.forEach(toppingField => {
				const toppingId = toppingField.id.replace(`toppingcountField-${menuId}-`, '');
				const storedToppingQty = sessionStorage.getItem(`topping_quantity_${menuId}_${toppingId}`);
				if (storedToppingQty !== null) {
					toppingField.value = storedToppingQty;
				}
			});
		}
	});

	updateTotal();
});