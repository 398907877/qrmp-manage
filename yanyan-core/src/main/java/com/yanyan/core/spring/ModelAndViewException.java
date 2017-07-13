package com.yanyan.core.spring;

import com.yanyan.core.ClassBasedBusinessException;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * ModelAndView中含有错误信息
 * User: Saintcy
 * Date: 2016/1/22
 * Time: 17:31
 */
public class ModelAndViewException extends ClassBasedBusinessException {
    private ModelAndView modelAndView;
    private String message;
    private Map<String, List<String>> errors;

    public ModelAndViewException(ModelAndView mv, Map<String, List<String>> errorsMap) {
        super("Model And View encounter errors");
        this.modelAndView = mv;


        this.errors = errorsMap;
        Map<String, Object> model = mv.getModel();
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof Errors) {
                Errors errors = (Errors) entry.getValue();
                //将其中一个错误，作为错误码即错误信息
                for (ObjectError error : errors.getAllErrors()) {
                    this.code = error.getCode();
                    this.message = error.getDefaultMessage();
                    return;
                }
            }
        }

        //获取详细的错误信息
        //Map<String, List<String>> errorsMap = new HashMap<String, List<String>>();
        /*for (Map.Entry<String, Object> entry : model.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof Errors) {
                Errors errors = (Errors) entry.getValue();
                if (errors.hasFieldErrors()) {
                    for (FieldError fieldError : errors.getFieldErrors()) {
                        String key = fieldError.getObjectName() + "." + fieldError.getField();
                        List<String> errorMessages = errorsMap.get(key);
                        if (errorMessages == null) {
                            errorMessages = new ArrayList<String>();
                        }
                        errorMessages.add(fieldError.getDefaultMessage());
                        errorsMap.put(key, errorMessages);
                    }
                }
                if (errors.hasGlobalErrors()) {
                    for (ObjectError objectError : errors.getGlobalErrors()) {
                        String key = objectError.getObjectName();
                        List<String> errorMessages = errorsMap.get(key);
                        if (errorMessages == null) {
                            errorMessages = new ArrayList<String>();
                        }
                        errorMessages.add(objectError.getDefaultMessage());
                        errorsMap.put(objectError.getObjectName(), errorMessages);
                    }
                }
            }
        }
        this.errors = errorsMap;*/
    }

    public ModelAndView getModelAndView() {
        return modelAndView;
    }

    @Override
    public String getMessage() {
        return this.message == null ? super.getMessage() : this.message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}
