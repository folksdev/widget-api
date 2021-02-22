package com.miro.widgetapi.dto;

public interface DtoConverter <K,V>{
    V convert(K model);
}
