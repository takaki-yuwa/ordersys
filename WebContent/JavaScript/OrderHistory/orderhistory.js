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
		return true; // 長さが違えば変更されている
	}

	for (let i = 0; i < newList.length; i++) {
		const oldHistory = oldList[i]?.orderHistoryInfo || [];
		const newHistory = newList[i]?.orderHistoryInfo || [];

		if (oldHistory.length !== newHistory.length) {
			return true; // 各注文の履歴数が違えば変更
		}

		// 中身を一つずつ比較（JSON文字列化で簡易的に比較）
		for (let j = 0; j < newHistory.length; j++) {
			if (JSON.stringify(oldHistory[j]) !== JSON.stringify(newHistory[j])) {
				return true; // 中身が違えば変更あり
			}
		}
	}
	return false; // すべて一致 → 変更なし
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
