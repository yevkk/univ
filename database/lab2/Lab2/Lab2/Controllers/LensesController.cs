using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using Lab2.Data;
using Lab2.Models;

namespace Lab2.Controllers
{
    public class LensesController : Controller
    {
        private readonly Lab2Context _context;

        public LensesController(Lab2Context context)
        {
            _context = context;
        }

        // GET: Lenses
        public async Task<IActionResult> Index()
        {
            return View(await _context.Lens.ToListAsync());
        }

        // GET: Lenses/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var lens = await _context.Lens
                .FirstOrDefaultAsync(m => m.Id == id);
            if (lens == null)
            {
                return NotFound();
            }

            return View(lens);
        }

        // GET: Lenses/Create
        public IActionResult Create()
        {
            ViewData["Mounting_id"] = new SelectList(_context.Set<Mounting>(), "Id", "Name");
            return View();
        }

        // POST: Lenses/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Manufacturer,Model,Aperture,Min_focal_length,Max_focal_length,Mounting_id")] Lens lens)
        {
            if (ModelState.IsValid)
            {
                _context.Add(lens);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(lens);
        }

        // GET: Lenses/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var lens = await _context.Lens.FindAsync(id);
            if (lens == null)
            {
                return NotFound();
            }
            ViewData["Mounting_id"] = new SelectList(_context.Set<Mounting>(), "Id", "Name");
            return View(lens);
        }

        // POST: Lenses/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Manufacturer,Model,Aperture,Min_focal_length,Max_focal_length,Mounting_id")] Lens lens)
        {
            if (id != lens.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(lens);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!LensExists(lens.Id))
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
            return View(lens);
        }

        // GET: Lenses/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var lens = await _context.Lens
                .FirstOrDefaultAsync(m => m.Id == id);
            if (lens == null)
            {
                return NotFound();
            }

            return View(lens);
        }

        // POST: Lenses/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var lens = await _context.Lens.FindAsync(id);
            _context.Lens.Remove(lens);
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool LensExists(int id)
        {
            return _context.Lens.Any(e => e.Id == id);
        }
    }
}
