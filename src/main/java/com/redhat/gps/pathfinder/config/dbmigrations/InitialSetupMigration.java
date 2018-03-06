package com.redhat.gps.pathfinder.config.dbmigrations;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.domain.Authority;
import com.redhat.gps.pathfinder.domain.User;
import com.redhat.gps.pathfinder.security.AuthoritiesConstants;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.Instant;

import static com.redhat.gps.pathfinder.web.api.StaticAPIImpl.MIN_ASSESSMENT_VALUES;

/**
 * Creates the initial database setup
 */
@ChangeLog(order = "001")
public class InitialSetupMigration {

    @ChangeSet(order = "01", author = "initiator", id = "01-addAuthorities")
    public void addAuthorities(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);
        mongoTemplate.save(adminAuthority);
        mongoTemplate.save(userAuthority);
    }

    @ChangeSet(order = "02", author = "initiator", id = "02-addUsers")
    public void addUsers(MongoTemplate mongoTemplate) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(AuthoritiesConstants.ADMIN);
        Authority userAuthority = new Authority();
        userAuthority.setName(AuthoritiesConstants.USER);

        User systemUser = new User();
        systemUser.setId("user-0");
        systemUser.setLogin("system");
        systemUser.setPassword("$2a$10$mE.qmcV0mFU5NcKh73TZx.z4ueI/.bDWbj0T1BYyqP481kGGarKLG");
        systemUser.setFirstName("");
        systemUser.setLastName("System");
        systemUser.setEmail("system@localhost");
        systemUser.setActivated(true);
        systemUser.setLangKey("en");
        systemUser.setCreatedBy(systemUser.getLogin());
        systemUser.setCreatedDate(Instant.now());
        systemUser.getAuthorities().add(adminAuthority);
        systemUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(systemUser);

        User anonymousUser = new User();
        anonymousUser.setId("user-1");
        anonymousUser.setLogin("anonymoususer");
        anonymousUser.setPassword("$2a$10$j8S5d7Sr7.8VTOYNviDPOeWX8KcYILUVJBsYV83Y5NtECayypx9lO");
        anonymousUser.setFirstName("Anonymous");
        anonymousUser.setLastName("User");
        anonymousUser.setEmail("anonymous@localhost");
        anonymousUser.setActivated(true);
        anonymousUser.setLangKey("en");
        anonymousUser.setCreatedBy(systemUser.getLogin());
        anonymousUser.setCreatedDate(Instant.now());
        mongoTemplate.save(anonymousUser);

        User adminUser = new User();
        adminUser.setId("user-2");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(systemUser.getLogin());
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(adminUser);

        User userUser = new User();
        userUser.setId("user-3");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("en");
        userUser.setCreatedBy(systemUser.getLogin());
        userUser.setCreatedDate(Instant.now());
        userUser.getAuthorities().add(userAuthority);
        mongoTemplate.save(userUser);
    }


    @ChangeSet(order = "03", author = "initiator", id = "03-addStaticData")
    public void addStaticData(MongoTemplate mongoTemplate) {

        Assessments staticMinAssessmentValue = new Assessments();
        staticMinAssessmentValue.setId(MIN_ASSESSMENT_VALUES);
        staticMinAssessmentValue.setARCHTYPE("3");
        staticMinAssessmentValue.setCLUSTER("3");
        staticMinAssessmentValue.setCOMMS("3");
        staticMinAssessmentValue.setCOMPLIANCE("4");
        staticMinAssessmentValue.setCONFIG("3");
        staticMinAssessmentValue.setCONTAINERS("2");
        staticMinAssessmentValue.setDEPLOY("5");
        staticMinAssessmentValue.setDEPS3RD("4");
        staticMinAssessmentValue.setDEPSHW("3");
        staticMinAssessmentValue.setDEPSIN("3");
        staticMinAssessmentValue.setDEPSOUT("3");
        staticMinAssessmentValue.setDEPSOS("3");
        staticMinAssessmentValue.setHA("3");
        staticMinAssessmentValue.setHEALTH("2");
        staticMinAssessmentValue.setLOGS("4");
        staticMinAssessmentValue.setMETRICS("4");
        staticMinAssessmentValue.setOWNER("3");
        staticMinAssessmentValue.setPROFILE("4");
        staticMinAssessmentValue.setRESILIENCY("2");
        staticMinAssessmentValue.setSECURITY("3");
        staticMinAssessmentValue.setSTATE("3");
        staticMinAssessmentValue.setTEST("3");

        mongoTemplate.save(staticMinAssessmentValue);
    }
}
