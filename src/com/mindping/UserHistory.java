package com.mindping;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

public class UserHistory implements Serializable {
	private static final long serialVersionUID = -8572368613750657852L;

	private static class Event implements Serializable {
		private static final long serialVersionUID = -1338539763974394581L;
	}

	public static class Ping extends Event {
		private static final long serialVersionUID = -5152983885106425305L;

		public Ping(Date date, PingType type, PingResponse response) {
			super();
			this.date = date;
			this.type = type;
			this.response = response;
		}

		private static enum PingType {
			ONE_WAY, TWO_WAY,
		}

		private static enum PingResponse {
			NONE, YES, NO,
		}

		final Date date;

		final PingType type;

		final PingResponse response;
	}

	public final List<Event> events = Lists.newArrayList();
}
