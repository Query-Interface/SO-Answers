using System;
using System.Diagnostics;
using System.Net;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using app.Models;

namespace app.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;

        public HomeController(ILogger<HomeController> logger)
        {
            _logger = logger;
        }

        public IActionResult Index()
        {
            byte[] value = new byte[0];

            string sVal = HttpContext.Session.GetString("lastVisit");;
            if (sVal == null) {
                sVal = "_";
            }

            HttpContext.Session.SetString("lastVisit", DateTime.UtcNow.ToString());
            string host = Dns.GetHostName();
            return Content($"Host: {host};  Last seen: {sVal}");
        }

        public IActionResult Privacy()
        {
            return View();
        }

        [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
        public IActionResult Error()
        {
            return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
        }
    }
}
