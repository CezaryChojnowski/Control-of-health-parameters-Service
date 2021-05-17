package pb.wi.cohp.domain.measure;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pb.wi.cohp.domain.measureParameter.MeasureParameter;
import pb.wi.cohp.domain.measureParameter.MeasureParameterRepository;
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
                        .test(test)
                .build()
        );
    }

    public void removeMeasure(Long id){
        List<MeasureParameter> measureParameterList = measureParameterRepository
                .findAllByMeasureId(id);
        measureParameterRepository.deleteAll(measureParameterList);
    }

    public List<Measure> getMeasuresByUser(String username){
        return measureRepository.findAllByUser_Username(username);
    }

    public Measure getMeasureById(Long id){
        return measureRepository.findById(id).get();
    }
}
