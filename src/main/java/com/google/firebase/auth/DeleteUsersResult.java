/*
 * Copyright 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.firebase.auth;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.firebase.auth.internal.BatchDeleteResponse;
import com.google.firebase.internal.NonNull;
import java.util.List;

/**
 * Represents the result of the {@link FirebaseAuth#deleteUsersAsync(List)} API.
 */
public final class DeleteUsersResult {

  private int successCount;
  private int failureCount;
  private List<ErrorInfo> errors;

  DeleteUsersResult(int users, BatchDeleteResponse response) {
    ImmutableList.Builder<ErrorInfo> errorsBuilder = ImmutableList.builder();
    List<BatchDeleteResponse.ErrorInfo> responseErrors = response.getErrors();
    if (responseErrors != null) {
      checkArgument(users >= responseErrors.size());
      for (BatchDeleteResponse.ErrorInfo error : responseErrors) {
        errorsBuilder.add(new ErrorInfo(error.getIndex(), error.getMessage()));
      }
    }
    errors = errorsBuilder.build();
    failureCount = errors.size();
    successCount = users - errors.size();
  }

  /**
   * Returns the number of users that were deleted successfully (possibly zero). Users that did
   * not exist prior to calling deleteUsersAsync() will be considered to be successfully
   * deleted.
   */ 
  public int getSuccessCount() {
    return successCount;
  }

  /**
   * Returns the number of users that failed to be deleted (possibly zero).
   */
  public int getFailureCount() {
    return failureCount;
  }

  /**
   * A list of {@link ErrorInfo} instances describing the errors that were encountered during
   * the deletion. Length of this list is equal to the return value of 
   * {@link #getFailureCount()}.
   *
   * @return A non-null list (possibly empty).
   */
  @NonNull
  public List<ErrorInfo> getErrors() {
    return errors;
  }
}
