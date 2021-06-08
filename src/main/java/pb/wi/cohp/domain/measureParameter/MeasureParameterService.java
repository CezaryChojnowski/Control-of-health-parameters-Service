package pb.wi.cohp.domain.measureParameter;

import org.springframework.stereotype.Service;
import pb.wi.cohp.domain.measure.Measure;
import pb.wi.cohp.domain.measure.MeasureRepository;
import pb.wi.cohp.domain.measure.MeasureService;
import pb.wi.cohp.domain.measureParameter.dto.MeasureParameterDTO;
import pb.wi.cohp.domain.measureParameter.dto.MeasuresToChartDTO;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.parameter.ParameterService;
import pb.wi.cohp.domain.range.Range;
import pb.wi.cohp.domain.range.RangeService;
import pb.wi.cohp.domain.test.Test;
import pb.wi.cohp.domain.test.TestService;
import pb.wi.cohp.domain.user.User;
import pb.wi.cohp.domain.user.UserService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

@Service
public class MeasureParameterService {

    final MeasureParameterRepository measureParameterRepository;

    final MeasureService measureService;

    final UserService userService;

    final ParameterService parameterService;

    final TestService testService;

    final MeasureRepository measureRepository;

    final RangeService rangeService;


    public MeasureParameterService(MeasureParameterRepository measureParameterRepository, MeasureService measureService, UserService userService, ParameterService parameterService, TestService testService, MeasureRepository measureRepository, RangeService rangeService) {
        this.measureParameterRepository = measureParameterRepository;
        this.measureService = measureService;
        this.userService = userService;
        this.parameterService = parameterService;
        this.testService = testService;
        this.measureRepository = measureRepository;
        this.rangeService = rangeService;
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
            MeasureParameter measureParameter = new MeasureParameter
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

    public void editMeasureParameter(MeasureParameterDTO measureParameterDTO){
        List<MeasureParameter> measureParameters = measureParameterRepository.findAllByMeasureId(measureParameterDTO.getId());
        for(MeasureParameter measureParameter: measureParameters){
            measureParameter.setValue(measureParameterDTO.getValues().get(measureParameter.getParameter().getId()));
            measureParameterRepository.save(measureParameter);
        }
        Measure measure = measureRepository.findByIdAndHiddenFalse(measureParameterDTO.getId()).get();
        measure.setDate(measureParameterDTO.getDate());
        measure.setNote(measureParameterDTO.getNote());
        measureRepository.save(measure);

    }

    public List<String> checkIfMeasuresIsOverRange(HashMap<Long, Double> values, String username){
        List<String> result = new ArrayList<>();
        for (Map.Entry<Long,Double> value : values.entrySet()){
            Parameter parameter = parameterService.findParameterById(value.getKey());
            User owner = userService.getUserByUsername(username);
            Optional<Range> range = rangeService.getByUserAndParameter(owner,parameter);
            if(range.isPresent()) {
                if (value.getValue() > range.get().getMaxValue() || value.getValue() < range.get().getMinValue()) {
                    result.add(parameter.getName() + " is over range");
                }
            }
        }
        return result;
    }

    public List<MeasureParameter> getDataMeasuresByParameterIdAndOwner(Long id, String username){
        return measureParameterRepository.findAllByParameter_IdAndMeasure_User_UsernameAndHiddenIsFalseOrderByMeasureDateAsc(id, username);
    }

    public List<List<?>> getArraysWithDataToChart(List<MeasuresToChartDTO> measuresToChartDTOS) {
        List<Double> values = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        for(MeasuresToChartDTO measuresToChartDTO: measuresToChartDTOS){
            values.add(measuresToChartDTO.getValue());
            String test = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(measuresToChartDTO.getMeasureDate());
            dates.add(test);
        }
        List<List<?>> result = new ArrayList<>();
        result.add(values);
        result.add(dates);
        return result;
    }
}
