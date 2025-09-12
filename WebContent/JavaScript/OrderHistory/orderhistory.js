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

// 新しい注文履歴が追加されているかどうかを判定する関数
function getTotalProductCount(historyList) {
	return historyList.reduce((total, history) => {
		const orderHistoryInfo = history.orderHistoryInfo || [];
		return total + orderHistoryInfo.length;
	}, 0);
}

function isProductCountIncreased(oldList, newList) {
	const oldCount = getTotalProductCount(oldList);
	const newCount = getTotalProductCount(newList);

	// 注文履歴数が増えていれば true
	return newCount > oldCount;
}

// ページ読み込み時に前回の注文履歴リストを復元（なければ空配列）
let previousHistoryList = JSON.parse(localStorage.getItem('previousHistoryList') || '[]');

async function fetchHistoryList() {
	try {
		const response = await fetch(contextPath + '/api/historylist');
		if (!response.ok) throw new Error('ネットワークエラー');

		const newHistoryList = await response.json();

		if (isProductCountIncreased(previousHistoryList, newHistoryList)) {
			console.log("新しい注文履歴が追加されました → リロード");
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
