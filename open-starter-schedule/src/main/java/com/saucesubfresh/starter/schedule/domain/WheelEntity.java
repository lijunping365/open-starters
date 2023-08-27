/*
 * Copyright © 2022 organization SauceSubFresh
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
package com.saucesubfresh.starter.schedule.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * @author lijunping
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WheelEntity {

    private Long taskId;

    private Long round;

    private String cron;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WheelEntity that = (WheelEntity) o;
        return Objects.equals(taskId, that.taskId) &&
                Objects.equals(round, that.round);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, round);
    }
}
