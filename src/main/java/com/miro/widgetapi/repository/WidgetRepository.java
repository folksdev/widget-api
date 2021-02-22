package com.miro.widgetapi.repository;

import com.miro.widgetapi.model.Widget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WidgetRepository extends JpaRepository<Widget, String>, CustomWidgetRepository {

    Page<Widget> findAllByOrderByZDesc(Pageable page);

}
