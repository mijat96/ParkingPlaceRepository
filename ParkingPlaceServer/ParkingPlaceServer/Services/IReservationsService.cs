﻿using ParkingPlaceServer.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace ParkingPlaceServer.Services
{
	public interface IReservationsService
	{
		List<Reservation> getReservations();
		void AddReservation(Reservation reservation);
		bool RemoveReservation(User loggedUser);
	}
}