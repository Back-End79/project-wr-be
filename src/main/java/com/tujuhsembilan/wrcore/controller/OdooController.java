package com.tujuhsembilan.wrcore.controller;

import com.tujuhsembilan.wrcore.service.OdooService;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/odoo")
public class OdooController {

    OdooService odooService;

    @Autowired
    public OdooController(OdooService odooService) {
        this.odooService = odooService;
    }

    @GetMapping("/version")
    public ResponseEntity<Object> getVersion() throws MalformedURLException, XmlRpcException {
        return ResponseEntity.ok(odooService.getEmployee());
    }

}
