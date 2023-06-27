package com.tujuhsembilan.wrcore.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lib.i18n.utility.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationConfig {

  private final MessageUtil msg;

  @Bean
  public ApplicationRunner init() {
    return args -> {
      log.info(msg.get("application.init"));
      // do something here
      log.info(msg.get("application.done"));
    };
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

}
