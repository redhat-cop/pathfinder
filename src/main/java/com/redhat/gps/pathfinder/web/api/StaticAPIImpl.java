package com.redhat.gps.pathfinder.web.api;

import com.redhat.gps.pathfinder.domain.Assessments;
import com.redhat.gps.pathfinder.repository.AssessmentsRepository;
import com.redhat.gps.pathfinder.web.api.model.AssessmentType;
import com.redhat.gps.pathfinder.web.api.model.AssessmentVals;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pathfinder")
public class StaticAPIImpl  implements StaticApi{
    private final Logger log = LoggerFactory.getLogger(StaticAPIImpl.class);
    public final static String MIN_ASSESSMENT_VALUES="MIN_ASSESSMENT_VALUE";
    private final AssessmentsRepository assmRepo;

    public StaticAPIImpl(AssessmentsRepository assmRepo) {
        this.assmRepo = assmRepo;
    }

    public ResponseEntity<AssessmentType> staticMinimumGet() {
        Assessments currAssm = assmRepo.findOne(MIN_ASSESSMENT_VALUES);
        AssessmentType resp = new AssessmentType();

        if (currAssm == null){
            log.error("Unable to retrive static assessment minimum values from database");
            return new ResponseEntity<AssessmentType>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        AssessmentVals newitem = new AssessmentVals();
        newitem.setARCHTYPE(currAssm.getARCHTYPE());
        newitem.setASSMENTNAME(currAssm.getASSMENTNAME());
        newitem.setBUSPRIORITY(currAssm.getBUSPRIORITY());
        newitem.setARCHTYPE(currAssm.getARCHTYPE());
        newitem.setCOMMS(currAssm.getCOMMS());
        newitem.setCOMPLIANCE(currAssm.getCOMPLIANCE());
        newitem.setCONFIG(currAssm.getCONFIG());
        newitem.setDEPLOY(currAssm.getDEPLOY());
        newitem.setDePS3RD(currAssm.getDEPS3RD());
        newitem.setDEPSHW(currAssm.getDEPSHW());
        newitem.setDEPSIN(currAssm.getDEPSIN());
        newitem.setDEPSOS(currAssm.getDEPSOS());
        newitem.setDEPSOUT(currAssm.getDEPSOUT());
        newitem.setCONTAINERS(currAssm.getCONTAINERS());
        newitem.setCLUSTER(currAssm.getCLUSTER());
        newitem.setHA(currAssm.getHA());
        newitem.setHEALTH(currAssm.getHEALTH());
        newitem.setLOGS(currAssm.getLOGS());
        newitem.setMETRICS(currAssm.getMETRICS());
        newitem.setNOTES(currAssm.getNOTES());
        newitem.setOWNER(currAssm.getOWNER());
        newitem.setPROFILE(currAssm.getPROFILE());
        newitem.setRESILIENCY(currAssm.getRESILIENCY());
        newitem.setSECURITY(currAssm.getSECURITY());
        newitem.setSTATE(currAssm.getSTATE());
        newitem.setTEST(currAssm.getTEST());
        newitem.setDEPSOUTLIST(currAssm.getDEPSOUTLIST());
        resp.setPayload(newitem);

        return new ResponseEntity<AssessmentType>(resp,HttpStatus.OK);
    }
}
