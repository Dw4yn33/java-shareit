package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
public class CommentDto {

    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CommentDto that = (CommentDto) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getText(), that.getText()) &&
                Objects.equals(getAuthorName(), that.getAuthorName()) &&
                (Objects.equals(getCreated(), that.getCreated()) ||
                        Duration.between(getCreated(), that.getCreated()).compareTo(Duration.ofNanos(1000)) < 1000);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText(), getAuthorName(), getCreated());
    }
}
