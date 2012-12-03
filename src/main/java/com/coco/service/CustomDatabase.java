package com.coco.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.coco.vo.AttributeVO;
import com.coco.vo.BaseVO;
import com.coco.vo.CellVO;
import com.coco.vo.ElementVO;
import com.coco.vo.InputVO;
import com.coco.vo.OutputVO;
import com.coco.vo.UserVO;
import org.apache.log4j.Logger;

public class CustomDatabase {

    // TODO http://accu.org/index.php/journals/236

	private static final Logger log = Logger.getLogger(CustomDatabase.class);

	// Conexión JDBC a la base de datos
	private final Connection con;

    public CustomDatabase() {
        con = getConnection();
    }

    public CustomDatabase(Connection con) {
        this.con = con;
    }

	private static Connection getConnection() {
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx.lookup("java:comp/env/jdbc/cocoDB");
			return ds.getConnection();
		} catch (NamingException s) {
			log.error("Class not found: " + s.toString());
		} catch (SQLException se) {
			log.error("Error when openning connection to database.");
			log.error(se.getMessage());
		}
        return null;
	}

	public static Connection getConnectionFromDriver() {
		try {
			Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection("jdbc:postgresql://localhost:5432/cocoDB", "smunoz", "");
        } catch (ClassNotFoundException s) {
            log.error("Class not found: " + s.toString());
        } catch (SQLException se) {
			log.error("Error when openning connection to database.");
			log.error(se.getMessage());
		}
        return null;
	}

	public void destroy() {
		try {
			con.close();
		} catch (SQLException se) {
			log.error("Error when closing the connection to the database.");
			log.error(se.getMessage());
		}
	}

	public List<BaseVO> getListCOCOProblems(int idUser) {
		List<BaseVO> listCOCOProblems = new ArrayList<BaseVO>();
		ResultSet result;
		try {
			String query = "SELECT * FROM coco_problems WHERE id_user = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, idUser);
			result = ps.executeQuery();

			while (result.next()) {
				int id = result.getInt("id_coco");
                String name = result.getString("coco_name");
                String description = result.getString("coco_description");
				BaseVO cocoProblem = new BaseVO(id, name, description);

				log.info(cocoProblem);

				listCOCOProblems.add(cocoProblem);
			}
            ps.close();
		} catch (SQLException se) {
			log.error("List COCO problems: Error in database.");
			log.error(se.getMessage());
		}
		return listCOCOProblems;
	}

	public InputVO getCOCOInput(int idCOCO) {
		InputVO cocoInput = null;

		try {
			String query = "SELECT * FROM coco_problems WHERE id_coco = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, idCOCO);
            ResultSet result = ps.executeQuery();

			// Sin resultado
			if (!result.next()) {
				throw new SQLException();
			}

			// Construir un nuevo inputVO
			int id = result.getInt("id_coco");
			String name = result.getString("coco_name");
			String description = result.getString("coco_description");
			int function = result.getInt("id_function");
			boolean negativeAllowed = result.getBoolean("negative_allowed");
			int equilibrium = result.getInt("equilibrium");
			String yAttribute = result.getString("y_name");
            ps.close();
            
			List<ElementVO> elements = getElements(id);
			List<AttributeVO> attributes = getAttributes(id, elements);
			for (ElementVO elto : elements) {
				elto.setCells(getMatrixRow(id, elto, attributes));
			}

			cocoInput = new InputVO(id, name, description, function, negativeAllowed, equilibrium, yAttribute);
			cocoInput.setElements(elements);
			cocoInput.setAttributes(attributes);
		} catch (SQLException se) {
			log.error("Get COCO input: Error in database.");
			log.error(se.getMessage());
		}
		return cocoInput;
	}

	public void updateCOCOInput(InputVO inCOCO, int idUser) {
		deleteCOCOInput(getCOCOInput(inCOCO.getId()));
		createCOCOInput(inCOCO, idUser);
	}

	public void deleteCOCOInput(InputVO inCOCO) {
		try {
			List<AttributeVO> attributes = inCOCO.getAttributes();
			List<ElementVO> elements = inCOCO.getElements();

			// Borrar elements_attributes
			String query = "DELETE FROM elements_attributes WHERE id_attribute=?";
			PreparedStatement ps = con.prepareStatement(query);
            for (AttributeVO attribute : attributes) {
                ps.setInt(1, attribute.getId());
                ps.executeUpdate();
            }
            ps.close();

			// Borrar elements
			query = "DELETE FROM elements WHERE id_element = ?";
			ps = con.prepareStatement(query);
			for (ElementVO elto : elements) {
				ps.setInt(1, elto.getId());
				ps.executeUpdate();
			}
            ps.close();

			// Borrar attributes
			query = "DELETE FROM attributes WHERE id_attribute = ?";
			ps = con.prepareStatement(query);
			for (AttributeVO attribute : attributes) {
				ps.setInt(1, attribute.getId());
				ps.executeUpdate();
			}
            ps.close();
            
			// Borrar coco_problems
			query = "DELETE FROM coco_problems WHERE id_coco = " + inCOCO.getId();
			Statement s = con.createStatement();
			s.executeUpdate(query);
            s.close();
		} catch (SQLException se) {
			log.error("Delete COCO input: Error in database.");
			log.error(se.getMessage());
		}
	}

	public void createCOCOInput(InputVO inCOCO, int idUser) {
		try {
			// Insertar el problema
			String query = "INSERT INTO coco_problems (id_coco, id_function, id_user, coco_name, coco_solution, "
					+ "coco_description, negative_allowed, equilibrium, y_name) "
					+ "VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, inCOCO.getIdFunction());
			ps.setInt(2, idUser);
			ps.setString(3, inCOCO.getName());
			ps.setDouble(4, 0.0);
			ps.setString(5, inCOCO.getDescription());
			ps.setBoolean(6, inCOCO.getNegativeAllowed());
			ps.setDouble(7, inCOCO.getEquilibrium());
			ps.setString(8, inCOCO.getAttributeY());
			ps.executeUpdate();
            ps.close();

			// Guardar el id_coco para utilizarlo mas tarde
			int id_coco = getSequenceValue("sq_coco_problems");

			// Insertar los atributos
			query = "INSERT INTO attributes (id_attribute, id_rule, attribute_name, optima) "
					+ "VALUES (DEFAULT, ?, ?, ?)";
			ps = con.prepareStatement(query);
			for (AttributeVO att : inCOCO.getAttributes()) {
				ps.setInt(1, att.getRankRule());
				ps.setString(2, att.getName());
				ps.setDouble(3, att.getOptima());
				ps.executeUpdate();

				// Guardar el id_attribute
				att.setId(getSequenceValue("sq_attributes"));
			}
            ps.close();

			// Insertar los elementos
			query = "INSERT INTO elements (id_element, id_coco, element_name, y_value)"
					+ "VALUES (DEFAULT, ?, ?, ?)";
			ps = con.prepareStatement(query);
			ps.setInt(1, id_coco);
			for (ElementVO elto : inCOCO.getElements()) {
				ps.setString(2, elto.getName());
				ps.setDouble(3, elto.getYvalue());
				ps.executeUpdate();

				// Guardar el id_element
				elto.setId(getSequenceValue("sq_elements"));
			}
            ps.close();

			// Insertar elements_attributes
			query = "INSERT INTO elements_attributes (id_attribute, id_element, value, ranking, ideal_value)"
					+ "VALUES (?, ?, ?, ?, ?)";
			ps = con.prepareStatement(query);
			for (ElementVO elto : inCOCO.getElements()) {
				for (int j = 0; j < inCOCO.getAttributes().size(); j++) {
					CellVO cell = elto.getCells().get(j);

					ps.setInt(1, inCOCO.getAttributes().get(j).getId());
					ps.setInt(2, elto.getId());
					ps.setDouble(3, cell.getValue());
					ps.setInt(4, cell.getRanking());
					ps.setDouble(5, cell.getIdealValue());
					ps.executeUpdate();
				}
			}
            ps.close();
		} catch (SQLException se) {
			log.error("Set COCO input: Error in database.");
			log.error(se.getMessage());
		}
	}

	public void setCOCOOutput(OutputVO outCOCO, InputVO inCOCO) {
		try {
			// Actualizar coco_problems
			String query = "UPDATE coco_problems " + "SET coco_solution = ? "
					+ "WHERE id_coco = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setDouble(1, outCOCO.getSolution());
			ps.setInt(2, outCOCO.getId());
			ps.executeUpdate();

			// actualizar elements_attributes
			query = "UPDATE elements_attributes " + "SET ideal_value = ? "
					+ "WHERE (id_attribute = ?) AND (id_element = ?)";
			ps = con.prepareStatement(query);
			for (ElementVO elto : inCOCO.getElements()) {
				for (AttributeVO att : inCOCO.getAttributes()) {
					CellVO cell = elto.getCells().get(inCOCO.getAttributes().indexOf(att));

					ps.setDouble(1, cell.getIdealValue());
					ps.setInt(2, att.getId());
					ps.setInt(3, elto.getId());
					ps.executeUpdate();
				}
			}
            ps.close();
		} catch (SQLException se) {
			log.error("Set COCO output: Error in database.");
			log.error(se.getMessage());
		}
	}

	private int getSequenceValue(String SequenceName) {
		int value = -1;
		try {
			String query = "SELECT last_value FROM " + SequenceName;
			Statement st = con.createStatement();
            ResultSet result = st.executeQuery(query);

			// Resultado vacio
			if (!result.next()) {
				throw new SQLException("The sequence does not exist: " + SequenceName);
			}

			value = result.getInt(1);
            st.close();
		} catch (SQLException se) {
			log.error("Get sequence: Error in database.");
			log.error(se.getMessage());
		}

		return value;
	}

	public String getRule(int idRule) {
		String rule = "";

		try {
			String query = "SELECT * FROM rank_rules WHERE id_rule = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, idRule);
            ResultSet result = ps.executeQuery();

			// Resultado vacio
			if (!result.next()) {
				throw new SQLException();
			}
			rule = result.getString("rule_name");
            ps.close();
		} catch (SQLException se) {
			log.error("Get rule: Error in database.");
			log.error(se.getMessage());
		}

		return rule;
	}

	public String getFunction(int idFunction) {
		String function = "";

		try {
			String query = "SELECT * FROM objective_functions WHERE id_function = ?";
			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, idFunction);

            ResultSet result = ps.executeQuery();

			// Resultado vacio
			if (!result.next()) {
				throw new SQLException();
			}
			function = result.getString("function_name");
            ps.close();
		} catch (SQLException se) {
			log.error("Get function: Error in database.");
			log.error(se.getMessage());
		}

		return function;
	}

	public List<BaseVO> getFunctions() {
		List<BaseVO> functions = new ArrayList<BaseVO>();

		try {
			String query = "SELECT * FROM objective_functions";
			Statement s = con.createStatement();

            ResultSet result = s.executeQuery(query);

			while (result.next()) {
				BaseVO function = 
					new BaseVO(result.getInt("id_function"), result.getString("function_name"), "");

				functions.add(function);
			}
            s.close();
		} catch (SQLException se) {
			log.error("Get Functions: Error in database.");
			log.error(se.getMessage());
		}

		return functions;
	}

	public List<BaseVO> getRankRules() {
		List<BaseVO> rankRules = new ArrayList<BaseVO>();

		try {
			String query = "SELECT * FROM rank_rules";
			Statement s = con.createStatement();

            ResultSet result = s.executeQuery(query);

			while (result.next()) {
				BaseVO rule = 
					new BaseVO(result.getInt("id_rule"), result.getString("rule_name"), "");

				rankRules.add(rule);
			}
            s.close();
		} catch (SQLException se) {
			log.error("Get rank rules: Error in database.");
			log.error(se.getMessage());
		}

		return rankRules;
	}

	public List<ElementVO> getElements(int idCOCO) {
		List<ElementVO> elements = new ArrayList<ElementVO>();

		try {
			String query = "SELECT * FROM elements WHERE id_coco = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, idCOCO);
            ResultSet result = ps.executeQuery();

			String name;
			double yValue;
			int id;
			while (result.next()) {
				name = result.getString("element_name");
				id = result.getInt("id_element");
				yValue = result.getDouble("y_value");

				log.info("Element: " + name + yValue);

				elements.add(new ElementVO(id, name, "", yValue));
			}
            ps.close();
		} catch (SQLException se) {
			log.error("List COCO problems: Error in database.");
			log.error(se.getMessage());
		}

		return elements;
	}

	public List<AttributeVO> getAttributes(int idCOCO, List<ElementVO> elements) {
		List<AttributeVO> attributes = new ArrayList<AttributeVO>();

		try {
			if (elements.isEmpty()) {
				return attributes;
			}

			String query = "SELECT * FROM elements_attributes WHERE (id_element = ?)";
			PreparedStatement ps = con.prepareStatement(query);

			// Todos los elementos tienen los mismos atributos, 
			// seleccionamos el primer element para la query
			// tiene que ser igual para cualquier elemento
			ps.setInt(1, elements.get(0).getId());

            ResultSet result = ps.executeQuery();

			int id;
			while (result.next()) {
				id = result.getInt("id_attribute");
				attributes.add(getAttribute(id));
			}
            ps.close();
		} catch (SQLException se) {
			log.error("List COCO problems: Error in database.");
			log.error(se.getMessage());
		}

		return attributes;
	}

	private AttributeVO getAttribute(int idAttribute) {
		AttributeVO attribute = null;

		try {
			String query = "SELECT * FROM attributes WHERE id_attribute = ?";
			PreparedStatement ps = con.prepareStatement(query);

			ps.setInt(1, idAttribute);

            ResultSet result = ps.executeQuery();

			// Resultado vacio
			if (!result.next()) {
				throw new SQLException();
			}

			attribute = new AttributeVO(
								idAttribute, 
								result.getString("attribute_name"), 
								"", 
								result.getDouble("optima"), result.getInt("id_rule"));
            ps.close();
		} catch (SQLException se) {
			log.error("Get attribute: Error in database.");
			log.error(se.getMessage());
		}

		return attribute;
	}

	public String getNameRule(Integer idRule) {
		String nameRule = null;

		try {
			String query = "SELECT * FROM rank_rules WHERE id_rule = ?";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, idRule);
            ResultSet result = ps.executeQuery();

			// Resultado vacio
			if (!result.next()) {
				throw new SQLException();
			}

			nameRule = result.getString("rule_name");
            ps.close();
		} catch (SQLException se) {
			log.error("Get rule name: Error in database.");
			log.error(se.getMessage());
		}

		return nameRule;
	}

	public List<CellVO> getMatrixRow(int idCOCO, ElementVO element,	List<AttributeVO> attributes) {
		List<CellVO> matrixRow = CellVO.getNewCellList();

		try {
			if ((element == null) || (attributes.size() == 0)) {
				return matrixRow;
			}

			// Obtener la lista de celdas para el elemento seleccionado
			String query = "SELECT id_attribute, value, ranking, ideal_value "
					+ "FROM elements_attributes " + "WHERE id_element = ? ";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, element.getId());
            ResultSet result = ps.executeQuery();

			double value, idealValue;
			int ranking, attributeId, col;

			while (result.next()) {
				attributeId = result.getInt("id_attribute");
				value = result.getDouble("value");
				idealValue = result.getDouble("ideal_value");
				ranking = result.getInt("ranking");

				col = indexOf(attributes, attributeId);

				log.info(element.getName() + "(" + col + "): " + value);

				matrixRow.add(
						col, 
						new CellVO(-1, element.getName() + "_" + attributeId, "", value, idealValue, ranking));
			}
            ps.close();
		} catch (SQLException se) {
			log.error("Get row matrix: Error in database.");
			log.error(se.getMessage());
		}

		return matrixRow;
	}

	public int indexOf(List<? extends BaseVO> array, Integer index) {
		for (BaseVO base : array) {
			if (base.getId() == index) {
				return array.indexOf(base);
			}
		}
		return -1;
	}

	public UserVO authenticate(String email, String password) {
		UserVO userVO = null;

		try {
			String query = "SELECT * FROM users WHERE (email = ?) AND (password = ?)";
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
            ResultSet result = ps.executeQuery();

			if (result.next()) {
				// construir userVO
				userVO = new UserVO(result.getInt("id_user"),
						result.getString("user_name"),
						result.getString("user_surname"),
						result.getString("email"), result.getString("password"));

				// Actualizar el ultimo login
				setLastLoginUser(userVO.getId());

				log.info("User login correct.");
			} else {
				log.info("Wrong user login.");
			}
            ps.close();
		} catch (SQLException se) {
			log.error("User: Error in database.");
			log.error(se.getMessage());
		}

		return userVO;
	}

	public void setLastLoginUser(int idUser) {
		try {
			// actualizar coco_problems
			String query = "UPDATE users " + "SET last_login = now() "
					+ "WHERE id_user = ?";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setInt(1, idUser);

			ps.executeUpdate();
            ps.close();
		} catch (SQLException se) {
			log.error("Set last login user: Error in database.");
			log.error(se.getMessage());
		}
	}

	public void saveUser(UserVO user) {
		try {
			String query = "INSERT INTO users (id_user, user_name, user_surname, password, email)"
					+ " VALUES (DEFAULT, ?, ?, ?, ?)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, user.getName());
			ps.setString(2, user.getLastName());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getEmailAdress());

			ps.executeUpdate();

			user.setId(getSequenceValue("sq_users"));
            ps.close();
		} catch (SQLException se) {
			log.error("Set user: Error in database.");
			log.error(se.getMessage());
		}
	}

	public UserVO getUser(String email, String password) {
		UserVO user = null;

		try {
			String query = "SELECT (id_user, user_name, user_surname, password, email)"
					+ "FROM users WHERE (id_user=?) AND (password=?)";

			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);

            ResultSet result = ps.executeQuery();

			if (result.next()) {
				user = new UserVO(result.getInt("id_user"),
						result.getString("user_name"),
						result.getString("user_surname"),
						result.getString("email"), result.getString("password"));
			}
            ps.close();
		} catch (SQLException se) {
			log.error("Set user: Error in database.");
			log.error(se.getMessage());
		}

		return user;
	}

    public boolean removeUser(int userId) {
        boolean result = false;
        try {
            String query = "DELETE FROM users WHERE id_user = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, userId);

            result = ps.execute();

            ps.close();
        } catch (SQLException se) {
            log.error("Remove user: Error in database.", se);
        } finally {
            return result;
        }
    }
    public boolean removeUser(String userName) {
        boolean result = false;
        try {
            String query = "DELETE FROM users WHERE user_name = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, userName);

            result = ps.execute();

            ps.close();
        } catch (SQLException se) {
            log.error("Remove user: Error in database.", se);
        } finally {
            return result;
        }
    }
}