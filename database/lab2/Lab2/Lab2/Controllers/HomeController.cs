using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using Microsoft.EntityFrameworkCore;
using System.Diagnostics;
using System.Linq;
using System.Text.Json;
using System.Text.Json.Serialization;
using Lab2.Data;
using Lab2.Models;



namespace Lab2.Controllers
{
    public class HomeController : Controller
    {
        private readonly ILogger<HomeController> _logger;
        private readonly Lab2Context _context;

        public HomeController(ILogger<HomeController> logger, Lab2Context context)
        {
            _logger = logger;
            _context = context;
        }

        public IActionResult RunQuery(string str, int num, string query_index)
        {
            ViewData["QueryResult"] = "";

            if (query_index == "1")
            {
                var result = _context.QueryResults1.FromSqlRaw("SELECT username, fullname FROM dbo.Account WHERE EXISTS " +
                                                               "(SELECT id FROM dbo.Post WHERE account_id = dbo.Account.id AND pulse > {0})",
                                                               num.ToString()).ToList();
                ViewData["QueryResult"] = result;
            }
            else if (query_index == "2")
            {
                var result = _context.QueryResults1.FromSqlRaw("SELECT username, fullname FROM dbo.Account WHERE NOT EXISTS " +
                                                               "(SELECT id FROM dbo.Post WHERE account_id = dbo.Account.id AND pulse <= {0})",
                                                               num.ToString()).ToList();
                ViewData["QueryResult"] = result;
            }
            else if (query_index == "3")
            {
                var result = _context.QueryResults1.FromSqlRaw("SELECT username, fullname FROM dbo.Account WHERE EXISTS  " +
                                                               "(SELECT id FROM dbo.Post WHERE account_id = dbo.Account.id AND " +
                                                               "lens_id IN (SELECT id FROM dbo.Lens WHERE max_focal_length > {0} AND " +
                                                               "mounting_id IN (SELECT id FROM dbo.Mounting WHERE name = {1})))",
                                                               num.ToString(), str).ToList();
                ViewData["QueryResult"] = result;
            }
            else if (query_index == "4")
            {
                var result = _context.QueryResults2.FromSqlRaw("SELECT DISTINCT dbo.Camera.model as c_model, dbo.Lens.model as l_model " +
                                                               "FROM dbo.Post JOIN dbo.Camera ON dbo.Camera.id = camera_id JOIN dbo.Lens ON dbo.Lens.id = lens_id WHERE " +
                                                               "account_id IN (SELECT id FROM dbo.Account WHERE username = {0}) ",
                                                               str).ToList();
                ViewData["QueryResult"] = result;
            }
            else if (query_index == "5")
            {
                var result = _context.QueryResults3.FromSqlRaw("SELECT manufacturer, model FROM dbo.Lens WHERE " +
                                                               "mounting_id IN ( " +
                                                               "SELECT dbo.Camera.mounting_id " +
                                                               "FROM dbo.Post JOIN dbo.Camera ON camera_id = dbo.Camera.id JOIN dbo.Account ON account_id = dbo.Account.id WHERE " +
                                                               "pulse > {0} AND dbo.Account.username = {1})",
                                                               num.ToString(), str).ToList();
                ViewData["QueryResult"] = result;
            }
            else if (query_index == "6")
            {
                var result = _context.QueryResults4.FromSqlRaw("SELECT username FROM dbo.Account AS X WHERE " +
                                                               "NOT EXISTS (SELECT * FROM dbo.Camera AS Y JOIN dbo.Mounting ON mounting_id = dbo.Mounting.id WHERE dbo.Mounting.name = {0} " +
                                                               "AND NOT EXISTS (SELECT * FROM dbo.Post WHERE " +
                                                               "account_id = X.id AND camera_id = Y.id))",
                                                               str).ToList();
                ViewData["QueryResult"] = result;
            }
            else if (query_index == "7")
            {
                var result = _context.QueryResults4.FromSqlRaw("SELECT username FROM dbo.Account AS X WHERE " +
                                                               "NOT EXISTS (SELECT * FROM dbo.Lens AS Y WHERE " +
                                                               "EXISTS(SELECT * FROM dbo.Post WHERE " +
                                                               "lens_id = Y.id AND account_id = X.id) " +
                                                               "AND NOT EXISTS (SELECT * FROM dbo.Post WHERE " +
                                                               "lens_id = Y.id AND account_id " +
                                                               "IN (SELECT id FROM dbo.Account WHERE username = {0})))",
                                                               str).ToList();
                ViewData["QueryResult"] = result;
            }
            else if (query_index == "8")
            {
                var result = _context.QueryResults3.FromSqlRaw("SELECT dbo.Lens.manufacturer as manufacturer, dbo.Lens.model as model " +
                                                               "FROM dbo.Post JOIN dbo.Lens ON lens_id = dbo.Lens.id JOIN dbo.Account ON account_id = dbo.Account.id " +
                                                               "WHERE dbo.Account.country = {0} " +
                                                               "GROUP BY dbo.Lens.manufacturer, dbo.Lens.model " +
                                                               "HAVING COUNT(DISTINCT dbo.Account.id) > 1",
                                                               str).ToList();
                ViewData["QueryResult"] = result;
            }




            return View();
        }

        public IActionResult Index()
        {
            return View();
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
