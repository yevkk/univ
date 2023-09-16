using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using DDB_CW.Data;
using DDB_CW.Models;

namespace DDB_CW.Controllers
{
    public class BookingsController : Controller
    {
        private readonly DDB_CWContext _context;

        public BookingsController(DDB_CWContext context)
        {
            _context = context;
        }

        // GET: Bookings
        public async Task<IActionResult> Index()
        {
              var  db_context = _context.bookings.Include(m => m.Employee).Include(m => m.Row).Include(m => m.Row.Hall);
              return _context.bookings != null ? 
                          View(await db_context.ToListAsync()) :
                          Problem("Entity set 'DDB_CWContext.bookings'  is null.");
        }

        // GET: Bookings/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.bookings == null)
            {
                return NotFound();
            }

            var booking = await _context.bookings
                .Include(m => m.Employee)
                .Include(m => m.Row)
                .Include(m => m.Row.Hall)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (booking == null)
            {
                return NotFound();
            }

            return View(booking);
        }

        // GET: Bookings/Create
        public IActionResult Create()
        {
            ViewData["employeesSL"] = new SelectList(_context.Set<Employee>(), "Id", "Desc");
            ViewData["rowsSL"] = new SelectList(_context.Set<Row>().Include(m => m.Hall), "Id", "Desc");
            return View();
        }

        // POST: Bookings/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Place,StartTs,EndTs,Comment,EmployeeId,RowId")] Booking booking)
        {
            if (ModelState.IsValid)
            {
                _context.Add(booking);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(booking);
        }

        // GET: Bookings/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.bookings == null)
            {
                return NotFound();
            }

            var booking = await _context.bookings.FindAsync(id);
            if (booking == null)
            {
                return NotFound();
            }
            ViewData["employeesSL"] = new SelectList(_context.Set<Employee>(), "Id", "Desc");
            ViewData["rowsSL"] = new SelectList(_context.Set<Row>().Include(m => m.Hall), "Id", "Desc");
            return View(booking);
        }

        // POST: Bookings/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Place,StartTs,EndTs,Comment,EmployeeId,RowId")] Booking booking)
        {
            if (id != booking.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(booking);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!BookingExists(booking.Id))
                    {
                        return NotFound();
                    }
                    else
                    {
                        throw;
                    }
                }
                return RedirectToAction(nameof(Index));
            }
            return View(booking);
        }

        // GET: Bookings/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.bookings == null)
            {
                return NotFound();
            }

            var booking = await _context.bookings
                .Include(m => m.Employee)
                .Include(m => m.Row)
                .Include(m => m.Row.Hall)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (booking == null)
            {
                return NotFound();
            }

            return View(booking);
        }

        // POST: Bookings/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.bookings == null)
            {
                return Problem("Entity set 'DDB_CWContext.bookings'  is null.");
            }
            var booking = await _context.bookings.FindAsync(id);
            if (booking != null)
            {
                _context.bookings.Remove(booking);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool BookingExists(int id)
        {
          return (_context.bookings?.Any(e => e.Id == id)).GetValueOrDefault();
        }
    }
}
