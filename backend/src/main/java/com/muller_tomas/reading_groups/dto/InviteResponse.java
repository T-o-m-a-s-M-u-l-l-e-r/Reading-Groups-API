package com.muller_tomas.reading_groups.dto;

public class InviteResponse {

    private Integer inviteId;
    private Integer groupId;
    private Integer userId;

    public InviteResponse() {
    }

    public InviteResponse(Integer inviteId, Integer groupId, Integer userId) {
        this.inviteId = inviteId;
        this.groupId = groupId;
        this.userId = userId;
    }

    public Integer getInviteId() {
        return inviteId;
    }

    public void setInviteId(Integer inviteId) {
        this.inviteId = inviteId;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}