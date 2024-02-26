package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Friends {

    @NonNull
    private Integer fromUserId;
    @NonNull
    private Integer toUserId;
    @NonNull
    private Boolean isMutual;

}
