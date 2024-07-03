/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.datastrato.gravitino.dto.responses;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.datastrato.gravitino.Catalog;
import com.datastrato.gravitino.NameIdentifier;
import com.datastrato.gravitino.authorization.Privileges;
import com.datastrato.gravitino.authorization.SecurableObject;
import com.datastrato.gravitino.authorization.SecurableObjects;
import com.datastrato.gravitino.dto.AuditDTO;
import com.datastrato.gravitino.dto.CatalogDTO;
import com.datastrato.gravitino.dto.MetalakeDTO;
import com.datastrato.gravitino.dto.SchemaDTO;
import com.datastrato.gravitino.dto.authorization.GroupDTO;
import com.datastrato.gravitino.dto.authorization.RoleDTO;
import com.datastrato.gravitino.dto.authorization.SecurableObjectDTO;
import com.datastrato.gravitino.dto.authorization.UserDTO;
import com.datastrato.gravitino.dto.rel.ColumnDTO;
import com.datastrato.gravitino.dto.rel.TableDTO;
import com.datastrato.gravitino.dto.rel.partitioning.Partitioning;
import com.datastrato.gravitino.dto.util.DTOConverters;
import com.datastrato.gravitino.rel.types.Types;
import com.google.common.collect.Lists;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class TestResponses {
  @Test
  void testBaseDefaultCode() throws IllegalArgumentException {
    BaseResponse base = new BaseResponse();
    assertEquals(0, base.getCode());
  }

  @Test
  void testBaseValidateNegativeNumber() throws IllegalArgumentException {
    BaseResponse base = new BaseResponse(-1);
    assertThrows(IllegalArgumentException.class, () -> base.validate());
  }

  @Test
  void testBaseValidate() throws IllegalArgumentException {
    BaseResponse base = new BaseResponse();
    base.validate(); // No exception thrown
  }

  @Test
  void testDropped() throws IllegalArgumentException {
    DropResponse drop = new DropResponse();

    assertFalse(drop.dropped());
  }

  @Test
  void testDroppedTrue() throws IllegalArgumentException {
    DropResponse drop = new DropResponse(true);

    assertTrue(drop.dropped());
  }

  @Test
  void testEntityListResponse() throws IllegalArgumentException {
    NameIdentifier[] identsA = {NameIdentifier.parse("TableA")};
    EntityListResponse entityList = new EntityListResponse(identsA);
    entityList.validate(); // No exception thrown
    NameIdentifier[] identsB = entityList.identifiers();
    assertEquals(1, identsB.length);
    assertEquals("TableA", identsB[0].name());
  }

  @Test
  void testEntityListResponseException() throws IllegalArgumentException {
    EntityListResponse entityList = new EntityListResponse();
    assertThrows(IllegalArgumentException.class, () -> entityList.validate());
  }

  @Test
  void testMetalakeResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    MetalakeDTO metalake = MetalakeDTO.builder().withName("Metalake").withAudit(audit).build();
    MetalakeResponse response = new MetalakeResponse(metalake);
    response.validate(); // No exception thrown
  }

  @Test
  void testMetalakeResponseException() throws IllegalArgumentException {
    MetalakeResponse response = new MetalakeResponse();
    assertThrows(IllegalArgumentException.class, () -> response.validate());
  }

  @Test
  void testMetalakeListResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    MetalakeDTO metalake = MetalakeDTO.builder().withName("Metalake").withAudit(audit).build();
    MetalakeListResponse response = new MetalakeListResponse(new MetalakeDTO[] {metalake});
    response.validate(); // No exception thrown
  }

  @Test
  void testMetalakeListResponseException() throws IllegalArgumentException {
    MetalakeListResponse response = new MetalakeListResponse();
    assertThrows(IllegalArgumentException.class, () -> response.validate());
  }

  @Test
  void testCatalogResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    CatalogDTO catalog =
        CatalogDTO.builder()
            .withName("CatalogA")
            .withComment("comment")
            .withType(Catalog.Type.RELATIONAL)
            .withProvider("test")
            .withAudit(audit)
            .build();
    CatalogResponse catalogResponse = new CatalogResponse(catalog);
    catalogResponse.validate(); // No exception thrown
  }

  @Test
  void testCatalogException() throws IllegalArgumentException {
    CatalogResponse catalog = new CatalogResponse();
    assertThrows(IllegalArgumentException.class, () -> catalog.validate());
  }

  @Test
  void testSchemaResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    SchemaDTO schema =
        SchemaDTO.builder().withName("SchemaA").withComment("comment").withAudit(audit).build();
    SchemaResponse schemaResponse = new SchemaResponse(schema);
    schemaResponse.validate(); // No exception thrown
  }

  @Test
  void testSchemaException() throws IllegalArgumentException {
    SchemaResponse schema = new SchemaResponse();
    assertThrows(IllegalArgumentException.class, () -> schema.validate());
  }

  @Test
  void testTableResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    ColumnDTO column =
        ColumnDTO.builder().withName("ColumnA").withDataType(Types.ByteType.get()).build();
    TableDTO table =
        TableDTO.builder()
            .withName("TableA")
            .withComment("comment")
            .withColumns(new ColumnDTO[] {column})
            .withAudit(audit)
            .withPartitioning(Partitioning.EMPTY_PARTITIONING)
            .build();
    TableResponse tableResponse = new TableResponse(table);
    tableResponse.validate(); // No exception thrown
  }

  @Test
  void testTableException() throws IllegalArgumentException {
    TableResponse table = new TableResponse();
    assertThrows(IllegalArgumentException.class, () -> table.validate());
  }

  @Test
  void testRestErrorResponse() throws IllegalArgumentException {
    ErrorResponse error = ErrorResponse.restError("Rest error");
    error.validate(); // No exception thrown
  }

  @Test
  void testIllegalArgumentsErrorResponse() throws IllegalArgumentException {
    ErrorResponse error = ErrorResponse.illegalArguments("illegal arguments error");
    error.validate(); // No exception thrown
  }

  @Test
  void testNotFoundErrorResponse() throws IllegalArgumentException {
    ErrorResponse error = ErrorResponse.notFound("error type", "not found error");
    error.validate(); // No exception thrown
  }

  @Test
  void testAlreadyExistsErrorResponse() throws IllegalArgumentException {
    ErrorResponse error = ErrorResponse.alreadyExists("error type", "already exists error");
    error.validate(); // No exception thrown
  }

  @Test
  void testNonEmptyErrorResponse() throws IllegalArgumentException {
    ErrorResponse error = ErrorResponse.nonEmpty("error type", "non empty error");
    error.validate(); // No exception thrown
  }

  @Test
  void testUnknownErrorResponse() throws IllegalArgumentException {
    ErrorResponse error = ErrorResponse.unknownError("unknown error");
    error.validate(); // No exception thrown
  }

  @Test
  void testOAuthTokenResponse() throws IllegalArgumentException {
    OAuth2TokenResponse response =
        new OAuth2TokenResponse("Bearer xx", null, "Bearer", null, null, null);
    response.validate();
  }

  @Test
  void testOAuthTokenException() throws IllegalArgumentException {
    OAuth2TokenResponse response = new OAuth2TokenResponse();
    assertThrows(IllegalArgumentException.class, () -> response.validate());
  }

  @Test
  void testOAuthErrorResponse() throws IllegalArgumentException {
    OAuth2ErrorResponse response = new OAuth2ErrorResponse("invalid_grant", "error");
    response.validate(); // No exception thrown
  }

  @Test
  void testOAuthErrorException() throws IllegalArgumentException {
    OAuth2ErrorResponse response = new OAuth2ErrorResponse();
    assertThrows(IllegalArgumentException.class, () -> response.validate());
  }

  @Test
  void testUserResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    UserDTO user = UserDTO.builder().withName("user1").withAudit(audit).build();
    UserResponse response = new UserResponse(user);
    response.validate(); // No exception thrown
  }

  @Test
  void testUserResponseException() throws IllegalArgumentException {
    UserResponse user = new UserResponse();
    assertThrows(IllegalArgumentException.class, () -> user.validate());
  }

  @Test
  void testGroupResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    GroupDTO group = GroupDTO.builder().withName("group1").withAudit(audit).build();
    GroupResponse response = new GroupResponse(group);
    response.validate(); // No exception thrown
  }

  @Test
  void testGroupResponseException() throws IllegalArgumentException {
    GroupResponse group = new GroupResponse();
    assertThrows(IllegalArgumentException.class, () -> group.validate());
  }

  @Test
  void testRoleResponse() throws IllegalArgumentException {
    AuditDTO audit =
        AuditDTO.builder().withCreator("creator").withCreateTime(Instant.now()).build();
    SecurableObject securableObject =
        SecurableObjects.ofCatalog("catalog", Lists.newArrayList(Privileges.UseCatalog.allow()));
    RoleDTO role =
        RoleDTO.builder()
            .withName("role1")
            .withSecurableObjects(new SecurableObjectDTO[] {DTOConverters.toDTO(securableObject)})
            .withAudit(audit)
            .build();
    RoleResponse response = new RoleResponse(role);
    response.validate(); // No exception thrown
  }

  @Test
  void testRoleResponseException() throws IllegalArgumentException {
    RoleResponse role = new RoleResponse();
    assertThrows(IllegalArgumentException.class, () -> role.validate());
  }
}
