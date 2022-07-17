package com.saucesubfresh.starter.schedule.domain;

import lombok.Data;

import java.util.Objects;

/**
 * @author: 李俊平
 * @Date: 2022-07-17 12:34
 */
@Data
public class WheelEntity {

    private Long taskId;

    private Long round;

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
