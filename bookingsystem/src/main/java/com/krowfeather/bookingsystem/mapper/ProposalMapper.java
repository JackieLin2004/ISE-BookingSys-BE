package com.krowfeather.bookingsystem.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.krowfeather.bookingsystem.entity.Proposal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ProposalMapper extends BaseMapper<Proposal> {
    @Select("select * from proposal where cid = #{id} and status = 0")
    List<Proposal> getAllWaitingProposal(Integer id);

    @Update("update proposal set status = #{i} where id = #{pid}")
    void updateStatus(Integer pid, int i);

    @Select("select * from proposal where cid = #{id} and status = 2")
    List<Proposal> getAllWithdrawProposal(Integer id);

    @Select("select * from proposal where cid = #{id} and status = 1")
    List<Proposal> getAllAcceptProposal(Integer id);

    @Update("update proposal set prepared=1 where id = #{pid}")
    void updatePrepared(Integer pid);

    @Select("select cid from proposal where id = #{pid}")
    Integer getCid(Integer pid);

    @Select("select * from proposal where cid = #{id} and status = 3")
    List<Proposal> getAllPaidProposal(Integer id);
}
