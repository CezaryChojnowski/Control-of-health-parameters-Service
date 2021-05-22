package pb.wi.cohp.domain.measureParameter;

import org.springframework.stereotype.Service;
import pb.wi.cohp.domain.measure.Measure;
import pb.wi.cohp.domain.measure.MeasureRepository;
import pb.wi.cohp.domain.measure.MeasureService;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestService;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.user.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MeasureParameterService {

    final MeasureParameterRepository measureParameterRepository;

    final MeasureService measureService;

    final UserService userService;

    final ParameterService parameterService;

    final TestService testService;

    final MeasureRepository measureRepository;


    public MeasureParameterService(MeasureParameterRepository measureParameterRepository, MeasureService measureService, UserService userService, ParameterService parameterService, TestService testService, MeasureRepository measureRepository) {
        this.measureParameterRepository = measureParameterRepository;
        this.measureService = measureService;
        this.userService = userService;
        this.parameterService = parameterService;
        this.testService = testService;
        this.measureRepository = measureRepository;
    }

    public void createMeasureParameter(Map<Long, Double> values,
                                                   String username,
                                                   LocalDate date,
                                                   String note,
                                       Long id){
        User user = userService.getUserByUsername(username);
        Test test = testService.findTestById(id);
        Measure measure = measureService.createMeasure(date, note, user, test);
        List<MeasureParameter> measureParameterList = new ArrayList<>();
        for (Map.Entry<Long,Double> value : values.entrySet()){
            Parameter parameter = parameterService.findParameterById(value.getKey());
            MeasureParameter measureParameter =                     new MeasureParameter
                    .MeasureParameterBuilder()
                    .measure(measure)
                    .parameter(parameter)
                    .hidden(false)
                    .value(value.getValue())
                    .build();
            measureParameterRepository.save(
                    measureParameter
            );
            measureParameterList.add(measureParameter);
        }
        measure.setMeasureParameterList(measureParameterList);
        measureRepository.save(measure);
    }
}
