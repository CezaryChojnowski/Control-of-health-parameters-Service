package pb.wi.cohp.domain.measure;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pb.wi.cohp.domain.measureParameter.MeasureParameter;
import pb.wi.cohp.domain.measureParameter.MeasureParameterRepository;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.user.User;

import java.time.LocalDate;
import java.util.List;

@Service
public class MeasureService {

    final MeasureRepository measureRepository;

    final MeasureParameterRepository measureParameterRepository;

    public MeasureService(MeasureRepository measureRepository, MeasureParameterRepository measureParameterRepository) {
        this.measureRepository = measureRepository;
        this.measureParameterRepository = measureParameterRepository;
    }

    public Measure createMeasure(LocalDate date, String note, User user, Test test){
        return measureRepository.save(
                new Measure
                        .MeasureBuilder()
                        .date(date)
                        .note(note)
                        .user(user)
                        .hidden(false)
                        .test(test)
                .build()
        );
    }

    public void removeMeasure(Long id){
        Measure measure = measureRepository.findByIdAndHiddenFalse(id).get();
        measure.setHidden(true);
        List<MeasureParameter> measureParameterList = measure.getMeasureParameterList();
        for(int i=0; i<measureParameterList.size(); i++){
            MeasureParameter measureParameter = measureParameterList.get(i);
            measureParameter.setHidden(true);
        }
    }

    public List<Measure> getMeasuresByUser(String username){
        return measureRepository.findAllByUser_UsernameAndHiddenFalse(username);
    }

    public Measure getMeasureById(Long id){
        return measureRepository.findByIdAndHiddenFalse(id).get();
    }
}
