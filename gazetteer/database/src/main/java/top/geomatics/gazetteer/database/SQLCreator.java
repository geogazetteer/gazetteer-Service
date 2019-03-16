/**
 * 
 */
package top.geomatics.gazetteer.database;

import org.apache.ibatis.jdbc.SQL;
/**
 * @author whudyj
 *
 */
public class SQLCreator {
	public static String selectAddressSql(int flag) {
		SQL sql = null;
		switch(flag)	{
		case 1:
			sql = new SQL() {
				{
					SELECT("T.*");
					FROM("#{tablename} T");
				}
			};
			break;
		case 2:
			sql = new SQL() {
				{
					SELECT("T.address");
					FROM("#{tablename} T");
				}
			};
			break;
		case 3:
			sql = new SQL() {
				{
					SELECT("T.address");
					FROM("#{tablename} T");
					WHERE("T.address = #{value}");
				}
			};
			break;
		case 4:
			sql = new SQL() {
				{
					SELECT("${fields}");
					FROM("${tablename}");
					ORDER_BY("P.ID");
					ORDER_BY("P.FULL_NAME");
				}
			};
			break;
		case 5:
			sql = new SQL() {
				{
					SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME");
					SELECT("P.LAST_NAME, P.CREATED_ON, P.UPDATED_ON");
					FROM("DMDZ P");
					FROM("ACCOUNT A");
					INNER_JOIN("DEPARTMENT D on D.ID = P.DEPARTMENT_ID");
					INNER_JOIN("COMPANY C on D.COMPANY_ID = C.ID");
					WHERE("P.ID = A.ID");
					WHERE("P.FIRST_NAME like ?");
					OR();
					WHERE("P.LAST_NAME like ?");
					GROUP_BY("P.ID");
					HAVING("P.LAST_NAME like ?");
					OR();
					HAVING("P.FIRST_NAME like ?");
					ORDER_BY("P.ID");
					ORDER_BY("P.FULL_NAME");
				}
			};
			break;
		case 6:
			sql = new SQL() {
				{
					SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME");
					SELECT("P.LAST_NAME, P.CREATED_ON, P.UPDATED_ON");
					FROM("DMDZ P");
					FROM("ACCOUNT A");
					INNER_JOIN("DEPARTMENT D on D.ID = P.DEPARTMENT_ID");
					INNER_JOIN("COMPANY C on D.COMPANY_ID = C.ID");
					WHERE("P.ID = A.ID");
					WHERE("P.FIRST_NAME like ?");
					OR();
					WHERE("P.LAST_NAME like ?");
					GROUP_BY("P.ID");
					HAVING("P.LAST_NAME like ?");
					OR();
					HAVING("P.FIRST_NAME like ?");
					ORDER_BY("P.ID");
					ORDER_BY("P.FULL_NAME");
				}
			};
			break;
		case 7:
			sql = new SQL() {
				{
					SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME");
					SELECT("P.LAST_NAME, P.CREATED_ON, P.UPDATED_ON");
					FROM("DMDZ P");
					FROM("ACCOUNT A");
					INNER_JOIN("DEPARTMENT D on D.ID = P.DEPARTMENT_ID");
					INNER_JOIN("COMPANY C on D.COMPANY_ID = C.ID");
					WHERE("P.ID = A.ID");
					WHERE("P.FIRST_NAME like ?");
					OR();
					WHERE("P.LAST_NAME like ?");
					GROUP_BY("P.ID");
					HAVING("P.LAST_NAME like ?");
					OR();
					HAVING("P.FIRST_NAME like ?");
					ORDER_BY("P.ID");
					ORDER_BY("P.FULL_NAME");
				}
			};
			break;
		case 8:
			sql = new SQL() {
				{
					SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME");
					SELECT("P.LAST_NAME, P.CREATED_ON, P.UPDATED_ON");
					FROM("DMDZ P");
					FROM("ACCOUNT A");
					INNER_JOIN("DEPARTMENT D on D.ID = P.DEPARTMENT_ID");
					INNER_JOIN("COMPANY C on D.COMPANY_ID = C.ID");
					WHERE("P.ID = A.ID");
					WHERE("P.FIRST_NAME like ?");
					OR();
					WHERE("P.LAST_NAME like ?");
					GROUP_BY("P.ID");
					HAVING("P.LAST_NAME like ?");
					OR();
					HAVING("P.FIRST_NAME like ?");
					ORDER_BY("P.ID");
					ORDER_BY("P.FULL_NAME");
				}
			};
			break;
		case 9:
			sql = new SQL() {
				{
					SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME");
					SELECT("P.LAST_NAME, P.CREATED_ON, P.UPDATED_ON");
					FROM("DMDZ P");
					FROM("ACCOUNT A");
					INNER_JOIN("DEPARTMENT D on D.ID = P.DEPARTMENT_ID");
					INNER_JOIN("COMPANY C on D.COMPANY_ID = C.ID");
					WHERE("P.ID = A.ID");
					WHERE("P.FIRST_NAME like ?");
					OR();
					WHERE("P.LAST_NAME like ?");
					GROUP_BY("P.ID");
					HAVING("P.LAST_NAME like ?");
					OR();
					HAVING("P.FIRST_NAME like ?");
					ORDER_BY("P.ID");
					ORDER_BY("P.FULL_NAME");
				}
			};
			break;
		case 10:
			sql = new SQL() {
				{
					SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FULL_NAME");
					SELECT("P.LAST_NAME, P.CREATED_ON, P.UPDATED_ON");
					FROM("DMDZ P");
					FROM("ACCOUNT A");
					INNER_JOIN("DEPARTMENT D on D.ID = P.DEPARTMENT_ID");
					INNER_JOIN("COMPANY C on D.COMPANY_ID = C.ID");
					WHERE("P.ID = A.ID");
					WHERE("P.FIRST_NAME like ?");
					OR();
					WHERE("P.LAST_NAME like ?");
					GROUP_BY("P.ID");
					HAVING("P.LAST_NAME like ?");
					OR();
					HAVING("P.FIRST_NAME like ?");
					ORDER_BY("P.ID");
					ORDER_BY("P.FULL_NAME");
				}
			};
			break;
		}
		return sql.toString();
	}

//	// Anonymous inner class
//	public static String deleteAddressSql() {
//		return new SQL() {
//			{
//				DELETE_FROM("Address");
//				WHERE("ID = ${id}");
//			}
//		}.toString();
//	}
//
//	// Builder / Fluent style
//	public static String insertAddressSql() {
//		String sql = new SQL().INSERT_INTO("Address").VALUES("ID, FIRST_NAME", "${id}, ${firstName}")
//				.VALUES("LAST_NAME", "${lastName}").toString();
//		return sql;
//	}

	// With conditionals (note the final parameters, required for the anonymous
	// inner class to access them)
	public static String selectAddressLike(final String id, final String firstName, final String lastName) {
		return new SQL() {
			{
				SELECT("P.ID, P.USERNAME, P.PASSWORD, P.FIRST_NAME, P.LAST_NAME");
				FROM("DMDZ P");
				if (id != null) {
					WHERE("P.ID like ${id}");
				}
				if (firstName != null) {
					WHERE("P.FIRST_NAME like ${firstName}");
				}
				if (lastName != null) {
					WHERE("P.LAST_NAME like ${lastName}");
				}
				ORDER_BY("P.LAST_NAME");
			}
		}.toString();
	}

	public static String deleteAddressSql() {
		return new SQL() {
			{
				DELETE_FROM("DMDZ");
				WHERE("ID = ${id}");
			}
		}.toString();
	}

	public static String insertAddressSql() {
		return new SQL() {
			{
				INSERT_INTO("DMDZ");
				VALUES("ID, FIRST_NAME", "${id}, ${firstName}");
				VALUES("LAST_NAME", "${lastName}");
			}
		}.toString();
	}

	public static String updateAddressSql() {
		return new SQL() {
			{
				UPDATE("DMDZ");
				SET("FIRST_NAME = ${firstName}");
				WHERE("ID = ${id}");
			}
		}.toString();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(selectAddressSql(4));
	}

}
