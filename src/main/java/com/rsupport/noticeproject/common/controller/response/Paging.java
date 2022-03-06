package com.rsupport.noticeproject.common.controller.response;

import lombok.Builder;
import lombok.Data;

@Data
public class Paging {
	private final Long totalCount;
	private final Integer currentPage;
	private final Integer perPage;
	private final Integer totalPage;

	@Builder
	public Paging(Long totalCount,
	              Integer currentPage,
	              Integer perPage,
	              Integer totalPage) {
		this.totalCount = totalCount;
		this.currentPage = currentPage;
		this.perPage = perPage;
		this.totalPage = totalPage;
	}
}
