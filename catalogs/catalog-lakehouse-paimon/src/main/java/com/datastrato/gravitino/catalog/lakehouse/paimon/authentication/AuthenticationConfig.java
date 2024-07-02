/*
 * Copyright 2024 Datastrato Pvt Ltd.
 * This software is licensed under the Apache License version 2.
 */

package com.datastrato.gravitino.catalog.lakehouse.paimon.authentication;

import com.datastrato.gravitino.Config;
import com.datastrato.gravitino.catalog.lakehouse.paimon.authentication.kerberos.KerberosConfig;
import com.datastrato.gravitino.config.ConfigBuilder;
import com.datastrato.gravitino.config.ConfigConstants;
import com.datastrato.gravitino.config.ConfigEntry;
import com.datastrato.gravitino.connector.PropertyEntry;
import com.google.common.collect.ImmutableMap;
import java.util.Map;

public class AuthenticationConfig extends Config {

  // The key for the authentication type, currently we support Kerberos and simple
  public static final String AUTH_TYPE_KEY = "authentication.type";

  public static final String IMPERSONATION_ENABLE_KEY = "authentication.impersonation-enable";

  enum AuthenticationType {
    SIMPLE,
    KERBEROS
  }

  public AuthenticationConfig(Map<String, String> properties) {
    super(false);
    loadFromMap(properties, k -> true);
  }

  public static final ConfigEntry<String> AUTH_TYPE_ENTRY =
      new ConfigBuilder(AUTH_TYPE_KEY)
          .doc(
              "The type of authentication for Paimon catalog, currently we only support simple and Kerberos")
          .version(ConfigConstants.VERSION_0_6_0)
          .stringConf()
          .createWithDefault("simple");

  public static final ConfigEntry<Boolean> ENABLE_IMPERSONATION_ENTRY =
      new ConfigBuilder(IMPERSONATION_ENABLE_KEY)
          .doc("Whether to enable impersonation for the Paimon catalog")
          .version(ConfigConstants.VERSION_0_6_0)
          .booleanConf()
          .createWithDefault(KerberosConfig.DEFAULT_IMPERSONATION_ENABLE);

  public String getAuthType() {
    return get(AUTH_TYPE_ENTRY);
  }

  public boolean isSimpleAuth() {
    return AuthenticationType.SIMPLE.name().equalsIgnoreCase(getAuthType());
  }

  public boolean isKerberosAuth() {
    return AuthenticationType.KERBEROS.name().equalsIgnoreCase(getAuthType());
  }

  public boolean isImpersonationEnabled() {
    return get(ENABLE_IMPERSONATION_ENTRY);
  }

  public static final Map<String, PropertyEntry<?>> AUTHENTICATION_PROPERTY_ENTRIES =
      new ImmutableMap.Builder<String, PropertyEntry<?>>()
          .put(
              IMPERSONATION_ENABLE_KEY,
              PropertyEntry.booleanPropertyEntry(
                  IMPERSONATION_ENABLE_KEY,
                  "Whether to enable impersonation for the Paimon catalog",
                  false,
                  true,
                  KerberosConfig.DEFAULT_IMPERSONATION_ENABLE,
                  false,
                  false))
          .put(
              AUTH_TYPE_KEY,
              PropertyEntry.stringImmutablePropertyEntry(
                  AUTH_TYPE_KEY,
                  "The type of authentication for Paimon catalog, currently we only support simple and Kerberos",
                  false,
                  null,
                  false,
                  false))
          .build();
}