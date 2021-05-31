package pb.wi.cohp;

import org.springframework.stereotype.Component;
import pb.wi.cohp.domain.measure.Measure;
import org.modelmapper.ModelMapper;
import pb.wi.cohp.domain.measure.dto.MeasureDTO;
import pb.wi.cohp.domain.measure.dto.UserMeasureDetailsDTO;
import pb.wi.cohp.domain.measureParameter.MeasureParameter;
import pb.wi.cohp.domain.measureParameter.dto.MeasureValuesDTO;

import java.util.ArrayList;
import java.util.List;

@Component
public class Mapper {

    final ModelMapper modelMapper;

    public Mapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public MeasureDTO convertToMeasureDTO(Measure measure){
        return modelMapper.map(measure, MeasureDTO.class);
    }

    public List<MeasureDTO> convertToMeasureDTOList(List<Measure> measures){
        List<MeasureDTO> result = new ArrayList<>();
        for(Measure measure: measures){
            result.add(modelMapper.map(measure, MeasureDTO.class));
        }
        return result;
    }

    public UserMeasureDetailsDTO convertToUserMeasureDetailsDTO(Measure measure){
            return new UserMeasureDetailsDTO(measure.getId(), measure.getTest().getName(), measure.getDate(), measure.getNote(), convertToMeasureValuesDTO(measure.getMeasureParameterList()));
    }

    public List<MeasureValuesDTO> convertToMeasureValuesDTO(List<MeasureParameter> measureParameters){
        List<MeasureValuesDTO> result = new ArrayList<>();
        for(MeasureParameter measureParameter: measureParameters){
            result.add(modelMapper.map(measureParameter, MeasureValuesDTO.class));
        }
        return result;
    }
}
