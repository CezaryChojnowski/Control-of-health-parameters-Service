package pb.wi.cohp.domain.range;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import pb.wi.cohp.config.error.exception.ObjectNotFoundException;
import pb.wi.cohp.domain.parameter.Parameter;
import pb.wi.cohp.domain.user.User;

import java.util.List;
import java.util.Optional;

@Service
public class RangeService {
    private static final int page_SIZE = 10;
    final RangeRepository rangeRepository;

    public RangeService(RangeRepository rangeRepository) {
        this.rangeRepository = rangeRepository;
    }

    public void createRange(User user, Parameter paramter){
        rangeRepository.save(
                new Range.RangeBuilder()
                        .maxValue((double) 0)
                        .minValue((double) 0)
                        .parameter(paramter)
                        .user(user)
                        .hidden(false)
                        .build()
        );
    }

    public Range getByParameter(Parameter parameter){
        return rangeRepository.findByParameter(parameter).get();
    }

    public Range getByUser(User user){
        return rangeRepository.findByUser(user);
    }

    public Optional<Range> getByUserAndParameter(User user, Parameter parameter){
        return rangeRepository.findByUserAndParameter(user, parameter);
    }

    public Optional<Range> getByParameterId(Parameter parameter){
        return rangeRepository.findByParameter(parameter);
    }

    public List<Range> getRangesByUsername(String username, int page){
        return rangeRepository.findAllByUser_UsernameAndHiddenIsFalse(username, PageRequest.of(page, page_SIZE));
    }

    public Range editRange(EditRangeDTO editRangeDTO) {
        Optional<Range> range = rangeRepository.findById(editRangeDTO.getId());
        if(range.isPresent()){
            Double min = editRangeDTO.getMinValue();
            Double max = editRangeDTO.getMaxValue();
            if(max < min){
                min = editRangeDTO.getMaxValue();
                max = editRangeDTO.getMinValue();
            }
            range.get().setMinValue(min);
            range.get().setMaxValue(max);
            return rangeRepository.save(range.get());
        }
        throw new ObjectNotFoundException("");
    }

    public Range getById(Long id){
        return rangeRepository.findById(id).get();
    }

    public void hideRange(Long id){
        Range range = rangeRepository.findById(id).get();
        range.setHidden(true);
        rangeRepository.save(range);
    }

    public List<Range> getRangesByParameterId(Long id){
        return rangeRepository.findAllByParameterId(id);
    }
}
