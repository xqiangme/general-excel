package com.tool.general.excel.dao.mapper;

import com.tool.general.excel.dao.bean.ExportTaskInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 导出任务表 Mapper
 */
@Repository
public interface ExportTaskInfoMapper {

    /**
     * 根据任务ID查询数据
     *
     * @param taskId taskId
     * @return ExportTaskInfo
     */
    ExportTaskInfo getByTaskId(@Param("taskId") String taskId);

    /**
     * 新增任务
     *
     * @param entity entity
     * @return int
     */
    int insertTask(ExportTaskInfo entity);

    /**
     * 任务成功更新
     *
     * @param taskDual    taskDual
     * @param taskId      taskId
     * @param downloadUrl downloadUrl
     * @param taskRows    taskRows
     * @param taskStatus  taskStatus
     * @return int
     */
    int updateSuccess(@Param("taskDual") Long taskDual, @Param("taskId") String taskId, @Param("downloadUrl") String downloadUrl, @Param("taskRows") Integer taskRows, @Param("taskStatus") Integer taskStatus);

    /**
     * 任务失败更新
     *
     * @param taskDual   taskDual
     * @param taskId     taskId
     * @param remarks    remarks
     * @param taskStatus taskStatus
     * @return int
     */
    int updateFail(@Param("taskDual") Long taskDual, @Param("taskId") String taskId, @Param("remarks") String remarks, @Param("taskStatus") Integer taskStatus);
}
