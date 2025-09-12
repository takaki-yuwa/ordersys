/**
 * 
 */
//要素の取得
const openPopupButton = document.getElementById('open-popup');
const closePopupButton = document.getElementById('close-popup');
const popupOverlay = document.getElementById('popup-overlay');
const popupContent = document.getElementById('popup-content');

//ポップアップを表示する
openPopupButton.addEventListener('click', () => {
	popupOverlay.classList.add('show');
	popupContent.classList.add('show');
});

//ポップアップを閉じる
closePopupButton.addEventListener('click', () => {
	popupOverlay.classList.remove('show');
	popupContent.classList.remove('show');
});

// 注文履歴の中身が変わっているかを判定する関数
function isOrderHistoryChanged(oldList, newList) {
	if (oldList.length !== newList.length) {
		return true;
	}

	for (let i = 0; i < newList.length; i++) {
		const oldItem = oldList[i] || {};
		const newItem = newList[i] || {};

		// 比較対象のフィールドを列挙
		if (
			oldItem.order_flag !== newItem.order_flag ||
			oldItem.product_quantity !== newItem.product_quantity ||
			JSON.stringify(oldItem.topping_details || []) !== JSON.stringify(newItem.topping_details || [])
		) {
			return true;
		}
	}

	return false;
}

// ページ読み込み時に前回の注文履歴リストを復元（なければ空配列）
let previousHistoryList = JSON.parse(localStorage.getItem('previousHistoryList') || '[]');

async function fetchHistoryList() {
	try {
		const response = await fetch(contextPath + '/api/historylist');
		if (!response.ok) throw new Error('ネットワークエラー');

		const newHistoryList = await response.json();

		if (isOrderHistoryChanged(previousHistoryList, newHistoryList)) {
			console.log("注文履歴に変更があります → リロード");
			// localStorageに新しい履歴リストを保存してからリロード
			localStorage.setItem('previousHistoryList', JSON.stringify(newHistoryList));
			location.reload();
			return;
		}

		// 変化がなければlocalStorageも更新
		previousHistoryList = newHistoryList;
		localStorage.setItem('previousHistoryList', JSON.stringify(newHistoryList));

	} catch (err) {
		console.error("履歴データ取得に失敗", err);
	}
}

window.addEventListener('load', fetchHistoryList);
setInterval(fetchHistoryList, 1000);
