package com.example.springbootbatch.support;

import com.example.springbootbatch.Constants;
import org.springframework.batch.core.DefaultJobKeyGenerator;
import org.springframework.batch.core.JobKeyGenerator;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.repository.dao.AbstractJdbcBatchMetadataDao;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by emelyans on 8/31/2017.
 */
public class CustomJobExecutionDao extends AbstractJdbcBatchMetadataDao {

    private static final String FIND_EXECUTION_ID_BY_PARAM = "SELECT max(JOB_EXECUTION_ID) from %PREFIX%JOB_EXECUTION_PARAMS where KEY_NAME = ? and STRING_VAL = ?";

    private JobKeyGenerator<JobParameters> jobKeyGenerator = new DefaultJobKeyGenerator();

    public Long getLastRerunExecutionId(final String jobName, final JobParameters jobParameters) {
        List<Long> lastRerunExecutionId = getJdbcTemplate().query(getQuery(FIND_EXECUTION_ID_BY_PARAM),
                new RowMapper<Long>() {
                    @Override
                    public Long mapRow(ResultSet resultSet, int i) throws SQLException {
                        return resultSet.getLong(0);
                    }
                }, Constants.BUSINESS_PARAMETERS_KEY_JOB_PARAMETER_NAME, jobKeyGenerator.generateKey(jobParameters));

        if (lastRerunExecutionId.isEmpty()) {
            return null;
        } else {
//            Assert.state(lastRerunExecutionId.size() == 1, "");
            return lastRerunExecutionId.get(0);
        }
    }

}
