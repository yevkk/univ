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
    public class CheckInsController : Controller
    {
        private readonly DDB_CWContext _context;

        public CheckInsController(DDB_CWContext context)
        {
            _context = context;
        }

        // GET: CheckIns
        public async Task<IActionResult> Index()
        {
            var db_context = _context.check_ins
                              .Include(m => m.Booking)
                              .Include(m => m.Booking.Employee)
                              .Include(m => m.Booking.Row)
                              .Include(m => m.Booking.Row.Hall);
              return _context.check_ins != null ? 
                          View(await db_context.ToListAsync()) :
                          Problem("Entity set 'DDB_CWContext.CheckIn'  is null.");
        }

        // GET: CheckIns/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.check_ins == null)
            {
                return NotFound();
            }

            var checkIn = await _context.check_ins
                .Include(m => m.Booking)
                .Include(m => m.Booking.Employee)
                .Include(m => m.Booking.Row)
                .Include(m => m.Booking.Row.Hall)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (checkIn == null)
            {
                return NotFound();
            }

            return View(checkIn);
        }

        // GET: CheckIns/Create
        public IActionResult Create()
        {
            ViewData["bookingsSL"] = new SelectList(_context.Set<Booking>().Include(m => m.Employee).Include(m => m.Row).Include(m => m.Row.Hall), "Id", "Desc");
            return View();
        }

        // POST: CheckIns/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Ts,BookingId")] CheckIn checkIn)
        {
            if (ModelState.IsValid)
            {
                _context.Add(checkIn);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(checkIn);
        }

        // GET: CheckIns/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.check_ins == null)
            {
                return NotFound();
            }

            var checkIn = await _context.check_ins.FindAsync(id);
            if (checkIn == null)
            {
                return NotFound();
            }
            ViewData["bookingsSL"] = new SelectList(_context.Set<Booking>().Include(m => m.Employee).Include(m => m.Row).Include(m => m.Row.Hall), "Id", "Desc");
            return View(checkIn);
        }

        // POST: CheckIns/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Ts,BookingId")] CheckIn checkIn)
        {
            if (id != checkIn.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(checkIn);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!CheckInExists(checkIn.Id))
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
            return View(checkIn);
        }

        // GET: CheckIns/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.check_ins == null)
            {
                return NotFound();
            }

            var checkIn = await _context.check_ins
                .Include(m => m.Booking)
                .Include(m => m.Booking.Employee)
                .Include(m => m.Booking.Row)
                .Include(m => m.Booking.Row.Hall)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (checkIn == null)
            {
                return NotFound();
            }

            return View(checkIn);
        }

        // POST: CheckIns/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.check_ins == null)
            {
                return Problem("Entity set 'DDB_CWContext.CheckIn'  is null.");
            }
            var checkIn = await _context.check_ins.FindAsync(id);
            if (checkIn != null)
            {
                _context.check_ins.Remove(checkIn);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool CheckInExists(int id)
        {
          return (_context.check_ins?.Any(e => e.Id == id)).GetValueOrDefault();
        }
    }
}
