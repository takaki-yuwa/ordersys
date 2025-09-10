package dao.order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.table.TableInfo;
import util.DBUtil;

public class OrderStateDAO {
	//情報取得
	public TableInfo selectTableInfo(String url_token) {
		String selectTableSql = "SELECT session_id,table_id,session_status FROM table_sessions WHERE url_token = ? AND session_status <> 'closed'";
		TableInfo tableInfo = null;
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement selectStmt = connection.prepareStatement(selectTableSql)) {

			selectStmt.setString(1, url_token);

			try (ResultSet rs = selectStmt.executeQuery()) {
				if (rs.next()) {
					tableInfo = new TableInfo(rs.getInt("session_id"), rs.getInt("table_id"), rs.getString("session_status"));
				}
			}

		} catch (SQLException e) {
			System.err.println("データベースの卓番情報取得中にエラーが発生しました。");
			System.err.println("卓番情報取得中にSQLエラーが発生しました: " + e.getMessage());
			System.err.println("SQL状態コード: " + e.getSQLState());
			System.err.println("エラーコード: " + e.getErrorCode());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("卓番情報取得中に予期しないエラーが発生しました。");
			e.printStackTrace();
		}
		return tableInfo;
	}

	//卓番、卓番セッションテーブルの状態更新
	public void updateSession(String url_token) {
		String updateSessionSql = "UPDATE table_sessions AS s JOIN table_master AS m ON s.table_id = m.table_id SET s.session_status = 'active', s.start_time = NOW(), m.table_status = 'active', m.updated_at = NOW() WHERE s.url_token = ? AND s.session_status <> 'closed'";
		try (Connection connection = DBUtil.getConnection();
				PreparedStatement updateStmt = connection.prepareStatement(updateSessionSql)) {

			updateStmt.setString(1, url_token);

			updateStmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("データベースの卓番・セッション状態更新中にエラーが発生しました。");
			System.err.println("卓番・セッション状態更新中にSQLエラーが発生しました: " + e.getMessage());
			System.err.println("SQL状態コード: " + e.getSQLState());
			System.err.println("エラーコード: " + e.getErrorCode());
			e.printStackTrace();
		} catch (Exception e) {
			System.err.println("卓番・セッション状態更新中に予期しないエラーが発生しました。");
			e.printStackTrace();
		}

	}
}
