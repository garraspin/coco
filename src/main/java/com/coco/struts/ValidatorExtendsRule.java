package com.coco.struts;

import java.util.Collection;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.validator.Arg;
import org.apache.commons.validator.Field;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResults;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.validator.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidatorExtendsRule {

  private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorExtendsRule.class);

    public ValidatorExtendsRule() {
    }


    /**
     * <p>Executes validation for another set of "inherited" rules.</p>
     *
     * @param  bean     The bean validation is being performed on.
     * @param  va       The <code>ValidatorAction</code> that is currently being performed.
     * @param  field    The <code>Field</code> object associated with the current
     *                  field being validated.
     * @param  errors   The <code>ActionMessages</code> object to add errors to if any
     *                  validation errors occur.
     * @param validator The <code>Validator</code> instance, used to access
     *                  other field values.
     * @param  request  Current request object.
     * @param  application  The Servlet Context
     * @return results of the validator.
     */
    public static Object validateExtends(Object bean,
                                         ValidatorAction va,
                                         Field field,
                                         ActionMessages errors,
                                         Validator validator,
                                         HttpServletRequest request,
                                         ServletContext application) {

        String fieldPrefix = field.getProperty().length() > 0 ? field.getProperty() : null;
        String formAndProperty = fieldPrefix == null ?  validator.getFormName() :  validator.getFormName() + "/" + fieldPrefix;

        // Get the validation key
        String key = field.getVarValue("extends");
        if (key == null || key.length() == 0) {
            LOGGER.error("'extends' var is missing for " + formAndProperty);
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.extends", formAndProperty));
            return Boolean.FALSE;
        }

        // Get the property value
        Object value = null;
        if (fieldPrefix == null) {
            value = bean;
        } else {
            try {
                value = PropertyUtils.getProperty(bean, field.getProperty());
            } catch(Exception e) {
                LOGGER.error("Error retrieving property '" + formAndProperty + "' " + e.getMessage(), e);
                errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.extends", formAndProperty));
            }
        }

        if (value == null) {
            LOGGER.error("Property '" + formAndProperty + "' is NULL");
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("errors.extends", formAndProperty));
            return Boolean.FALSE;
        }

        // Initialize the validator
        ActionMessages newErrors = new ActionMessages();
        Validator newValidator = Resources.initValidator(key, bean, application, request, newErrors, validator.getPage());


        // Is it an Array/Collection?
        Object[] values = null;
        if (value instanceof Collection) {
            values = ((Collection)value).toArray();
        } else if (value.getClass().isArray()) {
            values = (Object[])value;
        }

        // Execute the validator
        ValidatorResults results = null;
        if (values == null) {
            results = executeValidator(bean, -1, field, newValidator, errors, newErrors, fieldPrefix);
        } else {
            results = new ValidatorResults();
            for (int i = 0; i < values.length; i++) {
                fieldPrefix = field.getProperty() + "[" + i + "].";
                newValidator.setParameter(Validator.BEAN_PARAM, values[i]);
                ValidatorResults indexedResults = executeValidator(values[i], (i+1), field, newValidator, errors, newErrors, fieldPrefix);
                results.merge(indexedResults);
            }

        }

        return results;

    }


    /**
     *  Executes a validator.
     *
     * @param  key  The key of the validator to execute
     * @param  bean The bean validation is being performed on.
     * @param  application  The Servlet Context
     * @param  request Current request object.
     * @param  errors  The <code>ActionMessages</code> object to add errors to if any
     *                 validation errors occur.
     * @param  page   Page Number.
     * @param  prefix Prefix for error message properties.
     * @return results of the validator.
     */
    private static ValidatorResults executeValidator(Object bean,
                                                     int index,
                                                     Field field,
                                                     Validator validator,
                                                     ActionMessages errors,
                                                     ActionMessages newErrors,
                                                     String fieldPrefix) {
        // Validate
        ValidatorResults results = null;
        try {
            results = validator.validate();
        } catch (ValidatorException e) {
            LOGGER.error(e.getMessage(), e);
        }

        // Get Additional Argument
        Arg arg = field.getArg(0);
        String argKey = arg == null ? null : arg.getKey();
        Object argValue = null;
        if (argKey != null) {
            if ("#".equals(argKey)) {
                argValue = "" + index;
            } else {
                try {
                    argValue = PropertyUtils.getProperty(bean, argKey);
                } catch(Exception e) {
                    LOGGER.error("Error retrieving property '" + argKey + "' " + e.getMessage(), e);
                }
                argValue = argValue == null || "".equals(argValue) ? "???" : argValue;
            }
        }

        // Merge Errors
        if (newErrors.size() > 0) {

            if (fieldPrefix == null && argValue == null) {
                errors.add(newErrors);
            } else {
                Iterator properties = newErrors.properties();
                while (properties.hasNext()) {
                    String property = (String)properties.next();
                    Iterator messages = newErrors.get(property);
                    while (messages.hasNext()) {
                        ActionMessage msg = (ActionMessage)messages.next();
                        ActionMessage newMsg = msg;
                        String newProperty = fieldPrefix == null ? property : fieldPrefix + property;
                        if (argValue != null) {

                            // Add the additonal "argument" to the messages
                            Object[] args = msg.getValues();
                            Object[] newArgs = args;
                            if (argValue != null) {
                                int length = 0;
                                if (args != null) {
                                    for (int i = 0; i < args.length; i++) {
                                        if (args[i] != null)
                                            length =  i + 1;
                                    }
                                }

                                if (length == 0) {
                                    newArgs = new Object[] {argValue};
                                } else {
                                    newArgs = new Object[length + 1];
                                    System.arraycopy(args, 0, newArgs, 0, length);
                                    newArgs[length] = argValue;
                                }
                            }
                            newMsg = new ActionMessage(msg.getKey(), newArgs);
                        }
                        errors.add(newProperty, newMsg);
                    }
                }
            }
        }

        // Clear Errors
        newErrors.clear();

        // Return results
        return results;

    }

}