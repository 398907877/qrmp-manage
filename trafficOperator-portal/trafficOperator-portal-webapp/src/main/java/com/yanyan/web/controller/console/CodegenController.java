package com.yanyan.web.controller.console;

import com.yanyan.data.domain.console.*;
import com.yanyan.service.console.GenerateService;
import com.yanyan.service.console.SchemaService;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 自动代码生成器
 * User: Saintcy
 * Date: 2016/10/26
 * Time: 17:08
 */
@Slf4j
@Controller
@RequestMapping("/console/codegen")
public class CodegenController {
    @Autowired
    private SchemaService schemaService;
    @Autowired
    private GenerateService generateService;

    @RequestMapping("/index")
    public String index(Model model) {
        List<Schema> schemas = schemaService.getSchemaList();
        model.addAttribute("schemas", schemas);
        return "/console/codegen/index";
    }

    @RequestMapping("/{schema}/tables")
    @ResponseBody
    public Model tables(@PathVariable("schema") String schema) {
        try {
            List<Table> tables = schemaService.getTableList(schema);
            return DataResponse.success("tables", tables);
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/{schema}/{table}/columns")
    @ResponseBody
    public Model columns(@PathVariable("schema") String schema, @PathVariable("table") String table) {
        try {
            List<Column> columns = schemaService.getColumnList(schema, table);
            return DataResponse.success("columns", columns);
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/generate")
    @ResponseBody
    public Model generate(@RequestBody ModuleSetup setup){
        try {
            ModuleCodes codes = generateService.generate(setup);
            return DataResponse.success("codes", codes);
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
}
