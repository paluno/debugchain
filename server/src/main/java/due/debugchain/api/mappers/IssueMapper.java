package due.debugchain.api.mappers;

import due.debugchain.api.dto.IssueResource;
import due.debugchain.chain.IssueStruct;
import org.mapstruct.Mapper;

@Mapper
public interface IssueMapper {

    /**
     * Maps a IssueStruct to a IssueResource
     *
     * @param struct chain entity
     * @return mapped issue resource
     */
    IssueResource entityToResource(IssueStruct struct);

}
