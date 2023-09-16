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
    public class RowsController : Controller
    {
        private readonly DDB_CWContext _context;

        public RowsController(DDB_CWContext context)
        {
            _context = context;
        }

        // GET: Rows
        public async Task<IActionResult> Index()
        {
              var db_context = _context.rows.Include(m => m.Hall);
              return _context.rows != null ? 
                          View(await db_context.ToListAsync()) :
                          Problem("Entity set 'DDB_CWContext.rows'  is null.");
        }

        // GET: Rows/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.rows == null)
            {
                return NotFound();
            }

            var row = await _context.rows
                .Include(m => m.Hall)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (row == null)
            {
                return NotFound();
            }

            return View(row);
        }

        // GET: Rows/Create
        public IActionResult Create()
        {
            ViewData["hallsSL"] = new SelectList(_context.Set<Hall>(), "Id", "Name");
            return View();
        }

        // POST: Rows/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Mnemonic,PlacesNumber,HallId")] Row row)
        {
            if (ModelState.IsValid)
            {
                _context.Add(row);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(row);
        }

        // GET: Rows/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.rows == null)
            {
                return NotFound();
            }

            var row = await _context.rows.FindAsync(id);
            if (row == null)
            {
                return NotFound();
            }
            ViewData["hallsSL"] = new SelectList(_context.Set<Hall>(), "Id", "Name");
            return View(row);
        }

        // POST: Rows/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Mnemonic,PlacesNumber,HallId")] Row row)
        {
            if (id != row.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(row);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!RowExists(row.Id))
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
            return View(row);
        }

        // GET: Rows/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.rows == null)
            {
                return NotFound();
            }

            var row = await _context.rows
                .Include(m => m.Hall)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (row == null)
            {
                return NotFound();
            }

            return View(row);
        }

        // POST: Rows/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.rows == null)
            {
                return Problem("Entity set 'DDB_CWContext.rows'  is null.");
            }
            var row = await _context.rows.FindAsync(id);
            if (row != null)
            {
                _context.rows.Remove(row);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool RowExists(int id)
        {
          return (_context.rows?.Any(e => e.Id == id)).GetValueOrDefault();
        }
    }
}
