package com.tujuhsembilan.wrcore.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Service
@Slf4j
public class OdooService {

    @Value("${odoo.datasource.url}")
    String odooUrl;

    @Value("${odoo.datasource.database-name}")
    String odooDatabase;

    @Value("${odoo.datasource.username}")
    String odooUsername;

    @Value("${odoo.datasource.password}")
    String odooPassword;

    final XmlRpcClient client = new XmlRpcClient();

    int odooUid;

    @PostConstruct
    public void postConstruct() throws MalformedURLException, XmlRpcException {
        // authenticate odoo client
        final XmlRpcClientConfigImpl commonConfig = new XmlRpcClientConfigImpl();
        commonConfig.setServerURL(new URL(String.format("%s/xmlrpc/2/common", odooUrl)));
        odooUid = (int)client.execute(commonConfig, "authenticate",
                List.of(odooDatabase, odooUsername, odooPassword, emptyMap()));
    }

    public void getVersion() throws MalformedURLException, XmlRpcException {
        final XmlRpcClientConfigImpl commonConfig = new XmlRpcClientConfigImpl();
        commonConfig.setServerURL(new URL(String.format("%s/xmlrpc/2/common", odooUrl)));
        Object result = client.execute(commonConfig, "version", emptyList());
        JSONObject version = new JSONObject(result);
        log.info("version: {}", version);
    }

    public Object getEmployee() throws MalformedURLException, XmlRpcException {
        // odoo model config
        final XmlRpcClientConfigImpl modelConfig = new XmlRpcClientConfigImpl();
        modelConfig.setServerURL(new URL(String.format("%s/xmlrpc/2/object", odooUrl)));

        // query
        List<Object> query = List.of(
            List.of(
                List.of("company_id", "=", 1),
                List.of("department_id", "=", 7)
            )
        );

        // param
        HashMap<Object, Object> param = new HashMap<>();

        // fields to show
        param.put("fields", List.of("id", "nip", "name", "work_phone", "work_location", "company_id", "department_id"));

        // pagination
        param.put("offset", 0);
        param.put("limit", 5);

        // execute
        return asList((Object[]) client.execute(
            modelConfig,
            "execute_kw",
                List.of(odooDatabase, odooUid, odooPassword,
                    "hr.employee", "search_read",
                    query,
                    param
                )
            )
        );
    }

}
