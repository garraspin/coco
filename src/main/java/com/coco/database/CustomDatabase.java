package com.coco.database;

import java.math.BigDecimal;
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

	private static final Logger log = Logger.getLogger(CustomDatabase.class);
    private static final String COCODB_CONTEXT = "java:comp/env/jdbc/cocoDB";

	private final DataSource dataSource;

    public CustomDatabase() {
        dataSource = getDataSource();
    }

    public CustomDatabase(DataSource dataSource) {
        this.dataSource = dataSource;
    }

	public static DataSource getDataSource() {
		try {
			Context initCtx = new InitialContext();
			DataSource ds = (DataSource) initCtx.lookup(COCODB_CONTEXT);
			return ds;
		} catch (NamingException s) {
			log.error("Class not found: " + s.toString());
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
			log.error("Error when opening connection to database.");
			log.error(se);
		}
        return null;
	}

	public List<BaseVO> getListCOCOProblems(final int idUser) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<List<BaseVO>>() {
            @Override public List<BaseVO> execute(Connection con) {
                List<BaseVO> listCOCOProblems = new ArrayList<BaseVO>();
                ResultSet result = null;
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement("SELECT * FROM coco_problems WHERE id_user = ?");
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
                } catch (SQLException se) {
                    log.error("List COCO problems: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }
                return listCOCOProblems;
            }
        });
	}

    public InputVO getCOCOInput(final int idCOCO) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<InputVO>() {
            @Override public InputVO execute(Connection con) {
                InputVO cocoInput = null;
                PreparedStatement ps = null;
                ResultSet result = null;
                try {
                    ps = con.prepareStatement("SELECT * FROM coco_problems WHERE id_coco = ?");
                    ps.setInt(1, idCOCO);
                    result = ps.executeQuery();

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
                    List<AttributeVO> attributes = getAttributes(elements);
                    for (ElementVO elto : elements) {
                        elto.setCells(getMatrixRow(elto, attributes));
                    }

                    cocoInput = new InputVO(id, name, description, function, negativeAllowed, equilibrium, yAttribute);
                    cocoInput.setElements(elements);
                    cocoInput.setAttributes(attributes);
                } catch (SQLException se) {
                    log.error("Get COCO input: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }
                return cocoInput;
            }
        });
	}

	public void updateCOCOInput(InputVO inCOCO, int idUser) {
		deleteCOCOInput(getCOCOInput(inCOCO.getId()));
		createCOCOInput(inCOCO, idUser);
	}

	public void deleteCOCOInput(final InputVO inCOCO) {
        new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Void>() {
            @Override public Void execute(Connection con) {
                PreparedStatement ps = null;
                Statement s = null;
                try {
                    List<AttributeVO> attributes = inCOCO.getAttributes();
                    List<ElementVO> elements = inCOCO.getElements();

                    // Borrar elements_attributes
                    ps = con.prepareStatement("DELETE FROM elements_attributes WHERE id_attribute = ?");
                    for (AttributeVO attribute : attributes) {
                        ps.setInt(1, attribute.getId());
                        ps.executeUpdate();
                    }
                    ps.close();

                    // Borrar elements
                    ps = con.prepareStatement("DELETE FROM elements WHERE id_element = ?");
                    for (ElementVO elto : elements) {
                        ps.setInt(1, elto.getId());
                        ps.executeUpdate();
                    }
                    ps.close();

                    // Borrar attributes
                    ps = con.prepareStatement("DELETE FROM attributes WHERE id_attribute = ?");
                    for (AttributeVO attribute : attributes) {
                        ps.setInt(1, attribute.getId());
                        ps.executeUpdate();
                    }

                    // Borrar coco_problems
                    s = con.createStatement();
                    s.executeUpdate("DELETE FROM coco_problems WHERE id_coco = " + inCOCO.getId());
                } catch (SQLException se) {
                    log.error("Delete COCO input: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(ps, s);
                }

                return null;
            }});
	}

	public void createCOCOInput(final InputVO inCOCO, final int idUser) {
        new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Void>() {
            @Override public Void execute(Connection con) {
                PreparedStatement ps = null;
                try {
                    // Insertar el problema
                    ps = con.prepareStatement(
                            "INSERT INTO coco_problems (id_coco, id_function, id_user, coco_name, coco_solution, coco_description, negative_allowed, equilibrium, y_name)" +
                            " VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)");
                    ps.setInt(1, inCOCO.getIdFunction());
                    ps.setInt(2, idUser);
                    ps.setString(3, inCOCO.getName());
                    ps.setBigDecimal(4, new BigDecimal(0));
                    ps.setString(5, inCOCO.getDescription());
                    ps.setBoolean(6, inCOCO.getNegativeAllowed());
                    ps.setInt(7, inCOCO.getEquilibrium());
                    ps.setString(8, inCOCO.getAttributeY());
                    ps.executeUpdate();
                    ps.close();

                    // Guardar el id_coco para utilizarlo mas tarde
                    int id_coco = getSequenceValue("sq_coco_problems");

                    // Insertar los atributos
                    ps = con.prepareStatement(
                            "INSERT INTO attributes (id_attribute, id_rule, attribute_name, optima) " +
                                    "VALUES (DEFAULT, ?, ?, ?)");
                    for (AttributeVO att : inCOCO.getAttributes()) {
                        ps.setInt(1, att.getRankRule());
                        ps.setString(2, att.getName());
                        ps.setBigDecimal(3, att.getOptima());
                        ps.executeUpdate();

                        // Guardar el id_attribute
                        att.setId(getSequenceValue("sq_attributes"));
                    }
                    ps.close();

                    // Insertar los elementos
                    ps = con.prepareStatement(
                            "INSERT INTO elements (id_element, id_coco, element_name, y_value) " +
                                    "VALUES (DEFAULT, ?, ?, ?)");
                    ps.setInt(1, id_coco);
                    for (ElementVO elto : inCOCO.getElements()) {
                        ps.setString(2, elto.getName());
                        ps.setBigDecimal(3, elto.getYvalue());
                        ps.executeUpdate();

                        // Guardar el id_element
                        elto.setId(getSequenceValue("sq_elements"));
                    }
                    ps.close();

                    // Insertar elements_attributes
                    ps = con.prepareStatement(
                            "INSERT INTO elements_attributes (id_attribute, id_element, value, ranking, ideal_value) " +
                                    "VALUES (?, ?, ?, ?, ?)");
                    for (ElementVO elto : inCOCO.getElements()) {
                        for (int j = 0; j < inCOCO.getAttributes().size(); j++) {
                            CellVO cell = elto.getCells().get(j);

                            ps.setInt(1, inCOCO.getAttributes().get(j).getId());
                            ps.setInt(2, elto.getId());
                            ps.setBigDecimal(3, cell.getValue());
                            ps.setInt(4, cell.getRanking());
                            ps.setBigDecimal(5, cell.getIdealValue());
                            ps.executeUpdate();
                        }
                    }
                } catch (SQLException se) {
                    log.error("Set COCO input: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(ps);
                }

                return null;
            }});
	}

	public void setCOCOOutput(final OutputVO outCOCO, final InputVO inCOCO) {
        new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Void>() {
            @Override public Void execute(Connection con) {
                PreparedStatement ps = null;
                try {
                    // Actualizar coco_problems
                    ps = con.prepareStatement("UPDATE coco_problems SET coco_solution = ? WHERE id_coco = ?");
                    ps.setBigDecimal(1, outCOCO.getSolution());
                    ps.setInt(2, outCOCO.getId());
                    ps.executeUpdate();
                    ps.close();

                    // actualizar elements_attributes
                    ps = con.prepareStatement("UPDATE elements_attributes SET ideal_value = ? WHERE (id_attribute = ?) AND (id_element = ?)");
                    for (ElementVO elto : inCOCO.getElements()) {
                        for (AttributeVO att : inCOCO.getAttributes()) {
                            CellVO cell = elto.getCells().get(inCOCO.getAttributes().indexOf(att));

                            ps.setBigDecimal(1, cell.getIdealValue());
                            ps.setInt(2, att.getId());
                            ps.setInt(3, elto.getId());
                            ps.executeUpdate();
                        }
                    }
                } catch (SQLException se) {
                    log.error("Set COCO output: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(ps);
                }

                return null;
            }});
	}

	private int getSequenceValue(final String SequenceName) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Integer>() {
            @Override public Integer execute(Connection con) {
                int value = -1;
                Statement st = null;
                ResultSet result = null;

                try {
                    st = con.createStatement();
                    result = st.executeQuery("SELECT last_value FROM " + SequenceName);

                    // Resultado vacio
                    if (!result.next()) {
                        throw new SQLException("The sequence does not exist: " + SequenceName);
                    }

                    value = result.getInt(1);
                } catch (SQLException se) {
                    log.error("Get sequence: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, st);
                }

                return value;
            }});
	}

	public String getRule(final int idRule) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<String>() {
            @Override public String execute(Connection con) {
                String rule = "";
                PreparedStatement ps = null;
                ResultSet result = null;

                try {
                    ps = con.prepareStatement("SELECT * FROM rank_rules WHERE id_rule = ?");
                    ps.setInt(1, idRule);
                    result = ps.executeQuery();

                    // Resultado vacio
                    if (!result.next()) {
                        throw new SQLException();
                    }
                    rule = result.getString("rule_name");
                } catch (SQLException se) {
                    log.error("Get rule: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return rule;
            }});
	}

	public String getFunction(final int idFunction) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<String>() {
            @Override public String execute(Connection con) {
                String function = "";
                PreparedStatement ps = null;
                ResultSet result = null;

                try {
                    ps = con.prepareStatement("SELECT * FROM objective_functions WHERE id_function = ?");
                    ps.setInt(1, idFunction);
                    result = ps.executeQuery();

                    // Resultado vacio
                    if (!result.next()) {
                        throw new SQLException();
                    }
                    function = result.getString("function_name");
                } catch (SQLException se) {
                    log.error("Get function: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return function;
            }});
	}

	public List<BaseVO> getFunctions() {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<List<BaseVO>>() {
            @Override public List<BaseVO> execute(Connection con) {
                List<BaseVO> functions = new ArrayList<BaseVO>();
                Statement s = null;
                ResultSet result = null;

                try {
                    s = con.createStatement();
                    result = s.executeQuery("SELECT * FROM objective_functions");

                    while (result.next()) {
                        BaseVO function = new BaseVO(result.getInt("id_function"), result.getString("function_name"), "");
                        functions.add(function);
                    }
                } catch (SQLException se) {
                    log.error("Get Functions: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, s);
                }

                return functions;
            }});
	}

	public List<BaseVO> getRankRules() {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<List<BaseVO>>() {
            @Override public List<BaseVO> execute(Connection con) {
                List<BaseVO> rankRules = new ArrayList<BaseVO>();
                Statement s = null;
                ResultSet result = null;

                try {
                    s = con.createStatement();
                    result = s.executeQuery("SELECT * FROM rank_rules");

                    while (result.next()) {
                        BaseVO rule = new BaseVO(result.getInt("id_rule"), result.getString("rule_name"), "");
                        rankRules.add(rule);
                    }
                } catch (SQLException se) {
                    log.error("Get rank rules: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, s);
                }

                return rankRules;
            }});
	}

	public List<ElementVO> getElements(final int idCOCO) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<List<ElementVO>>() {
            @Override public List<ElementVO> execute(Connection con) {
                List<ElementVO> elements = new ArrayList<ElementVO>();
                PreparedStatement ps = null;
                ResultSet result = null;

                try {
                    String query = "SELECT * FROM elements WHERE id_coco = ?";
                    ps = con.prepareStatement(query);
                    ps.setInt(1, idCOCO);
                    result = ps.executeQuery();

                    while (result.next()) {
                        String name = result.getString("element_name");
                        int id = result.getInt("id_element");
                        BigDecimal yValue = result.getBigDecimal("y_value");

                        log.info("Element: " + name + yValue);

                        elements.add(new ElementVO(id, name, "", yValue));
                    }
                } catch (SQLException se) {
                    log.error("List COCO problems: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return elements;
            }});
	}

	public List<AttributeVO> getAttributes(final List<ElementVO> elements) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<List<AttributeVO>>() {
            @Override public List<AttributeVO> execute(Connection con) {
                List<AttributeVO> attributes = new ArrayList<AttributeVO>();
                PreparedStatement ps = null;
                ResultSet result = null;

                if (elements.isEmpty()) {
                    return attributes;
                }

                try {

                    String query = "SELECT * FROM elements_attributes WHERE (id_element = ?)";
                    ps = con.prepareStatement(query);
                    // Todos los elementos tienen los mismos atributos, seleccionamos el primer element para la query
                    // tiene que ser igual para cualquier elemento
                    ps.setInt(1, elements.get(0).getId());
                    result = ps.executeQuery();

                    while (result.next()) {
                        int id = result.getInt("id_attribute");
                        attributes.add(getAttribute(id));
                    }
                } catch (SQLException se) {
                    log.error("List COCO problems: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return attributes;
            }});
	}

	private AttributeVO getAttribute(final int idAttribute) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<AttributeVO>() {
            @Override public AttributeVO execute(Connection con) {
                AttributeVO attribute = null;
                PreparedStatement ps = null;
                ResultSet result = null;

                try {
                    String query = "SELECT * FROM attributes WHERE id_attribute = ?";
                    ps = con.prepareStatement(query);
                    ps.setInt(1, idAttribute);
                    result = ps.executeQuery();

                    // Resultado vacio
                    if (!result.next()) {
                        throw new SQLException();
                    }

                    attribute = new AttributeVO(idAttribute, result.getString("attribute_name"), "",
                            result.getBigDecimal("optima"), result.getInt("id_rule"));
                } catch (SQLException se) {
                    log.error("Get attribute: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return attribute;
            }});
	}

	public String getNameRule(final int idRule) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<String>() {
            @Override public String execute(Connection con) {
                PreparedStatement ps = null;
                ResultSet result = null;

                try {
                    String query = "SELECT * FROM rank_rules WHERE id_rule = ?";
                    ps = con.prepareStatement(query);
                    ps.setInt(1, idRule);
                    result = ps.executeQuery();

                    // Resultado vacio
                    if (!result.next()) {
                        throw new SQLException();
                    }

                    return result.getString("rule_name");
                } catch (SQLException se) {
                    log.error("Get rule name: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return "";
            }});
	}

	public List<CellVO> getMatrixRow(final ElementVO element, final List<AttributeVO> attributes) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<List<CellVO>>() {
            @Override public List<CellVO> execute(Connection con) {
                List<CellVO> matrixRow = CellVO.getNewCellList();
                PreparedStatement ps = null;
                ResultSet result = null;

                if ((element == null) || (attributes.size() == 0)) {
                    return matrixRow;
                }

                try {
                    // Obtener la lista de celdas para el elemento seleccionado
                    ps = con.prepareStatement("SELECT * FROM elements_attributes WHERE id_element = ? ");
                    ps.setInt(1, element.getId());
                    result = ps.executeQuery();

                    while (result.next()) {
                        int attributeId = result.getInt("id_attribute");
                        BigDecimal value = result.getBigDecimal("value");
                        BigDecimal idealValue = result.getBigDecimal("ideal_value");
                        int ranking = result.getInt("ranking");
                        int col = indexOf(attributes, attributeId);

                        log.info(element.getName() + "(" + col + "): " + value);
                        matrixRow.add(col, new CellVO(-1, element.getName() + "_" + attributeId, "", value, idealValue, ranking));
                    }
                } catch (SQLException se) {
                    log.error("Get row matrix: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return matrixRow;
            }});
	}

	public UserVO authenticate(final String email, final String password) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<UserVO>() {
            @Override public UserVO execute(Connection con) {
                UserVO userVO = null;
                PreparedStatement ps = null;
                ResultSet result = null;

                try {
                    String query = "SELECT * FROM users WHERE (email = ?) AND (password = ?)";
                    ps = con.prepareStatement(query);
                    ps.setString(1, email);
                    ps.setString(2, password);
                    result = ps.executeQuery();

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
                } catch (SQLException se) {
                    log.error("User: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return userVO;
            }});
	}

	public void setLastLoginUser(final int idUser) {
        new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Void>() {
            @Override public Void execute(Connection con) {
                PreparedStatement ps = null;
                try {
                    // actualizar coco_problems
                    ps = con.prepareStatement("UPDATE users SET last_login = now() WHERE id_user = ?");
                    ps.setInt(1, idUser);
                    ps.executeUpdate();
                    ps.close();
                } catch (SQLException se) {
                    log.error("Set last login user: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(ps);
                }
                return null;
            }});
	}

	public void saveUser(final UserVO user) {
        new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Void>() {
            @Override public Void execute(Connection con) {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement(
                            "INSERT INTO users (id_user, user_name, user_surname, password, email) VALUES (DEFAULT, ?, ?, ?, ?)");
                    ps.setString(1, user.getName());
                    ps.setString(2, user.getLastName());
                    ps.setString(3, user.getPassword());
                    ps.setString(4, user.getEmailAdress());

                    ps.executeUpdate();
                    user.setId(getSequenceValue("sq_users"));
                } catch (SQLException se) {
                    log.error("Set user: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(ps);
                }

                return null;
            }});
	}

	public UserVO getUser(final String email, final String password) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<UserVO>() {
            @Override public UserVO execute(Connection con) {
                PreparedStatement ps = null;
                ResultSet result = null;

                try {
                    ps = con.prepareStatement("SELECT * FROM users WHERE (email=?) AND (password=?)");
                    ps.setString(1, email);
                    ps.setString(2, password);
                    result = ps.executeQuery();
                    if (result.next()) {
                        return new UserVO(result.getInt("id_user"), result.getString("user_name"),
                                result.getString("user_surname"), result.getString("email"), result.getString("password"));
                    }
                } catch (SQLException se) {
                    log.error("Set user: Error in database.");
                    log.error(se.getMessage());
                } finally {
                    closeStatements(result, ps);
                }

                return null;
            }});
	}

    public boolean removeUser(final int userId) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Boolean>() {
            @Override public Boolean execute(Connection con) {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement("DELETE FROM users WHERE id_user = ?");
                    ps.setInt(1, userId);
                    return ps.execute();
                } catch (SQLException se) {
                    log.error("Remove user: Error in database.", se);
                } finally {
                    closeStatements(ps);
                }

                return false;
            }});
    }

    public boolean removeUser(final String userName) {
        return new ConnectionTemplate(dataSource).execute(new ConnectionTemplate.Template<Boolean>() {
            @Override public Boolean execute(Connection con) {
                PreparedStatement ps = null;
                try {
                    ps = con.prepareStatement("DELETE FROM users WHERE user_name = ?");
                    ps.setString(1, userName);
                    return ps.execute();
                } catch (SQLException se) {
                    log.error("Remove user: Error in database.", se);
                } finally {
                    closeStatements(ps);
                }
                return false;
            }});


    }

    private int indexOf(List<? extends BaseVO> array, Integer index) {
        for (BaseVO base : array) {
            if (base.getId() == index) {
                return array.indexOf(base);
            }
        }
        return -1;
    }

    private void closeStatements(Statement... statements) {
        closeStatements(null, statements);
    }

    private void closeStatements(ResultSet resultSet, Statement... statements) {
        for (Statement statement : statements) {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    log.error("Error when closing prepareStatement.");
                    log.error(e);
                }
            }
        }
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                log.error("Error when closing resultSet.");
                log.error(e);
            }
        }
    }
}