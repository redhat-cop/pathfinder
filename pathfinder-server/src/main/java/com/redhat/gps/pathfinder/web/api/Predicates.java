package com.redhat.gps.pathfinder.web.api;

import com.google.common.base.Predicate;
import com.redhat.gps.pathfinder.domain.Applications;
import com.redhat.gps.pathfinder.web.api.model.ApplicationType.StereotypeEnum;

public class Predicates{

  public static final Predicate<Applications> assessableApplications = new Predicate<Applications>() {
  	@Override public boolean apply(Applications app) {
        return app.getStereotype().equals(StereotypeEnum.TARGETAPP.toString());
  }};
}
