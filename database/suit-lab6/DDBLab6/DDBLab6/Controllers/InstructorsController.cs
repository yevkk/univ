using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Rendering;
using Microsoft.EntityFrameworkCore;
using DDBLab6.Data;
using DDBLab6.Models;

namespace DDBLab6.Controllers
{
    public class InstructorsController : Controller
    {
        private readonly DDBLab6Context _context;

        public InstructorsController(DDBLab6Context context)
        {
            _context = context;
        }

        // GET: Instructors
        public async Task<IActionResult> Index()
        {
            var db_context = _context.instructors.Include(m => m.Car);
            return _context.instructors != null ? 
                          View(await db_context.ToListAsync()) :
                          Problem("Entity set 'DDBLab6Context.Instructor'  is null.");
        }

        // GET: Instructors/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null || _context.instructors == null)
            {
                return NotFound();
            }

            var instructor = await _context.instructors
                .Include(m => m.Car)
                .FirstOrDefaultAsync(m => m.Id == id);
            if (instructor == null)
            {
                return NotFound();
            }

            return View(instructor);
        }

        // GET: Instructors/Create
        public IActionResult Create()
        {
            ViewData["carsSL"] = new SelectList(_context.Set<Car>(), "Id", "Desc");

            return View();
        }

        // POST: Instructors/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name,Experience,CertificateNumber,Phone,DedicatedCarId")] Instructor instructor)
        {
            if (ModelState.IsValid)
            {
                _context.Add(instructor);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(instructor);
        }

        // GET: Instructors/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null || _context.instructors == null)
            {
                return NotFound();
            }

            var instructor = await _context.instructors.FindAsync(id);
            if (instructor == null)
            {
                return NotFound();
            }

            ViewData["carsSL"] = new SelectList(_context.Set<Car>(), "Id", "Desc");
            return View(instructor);
        }

        // POST: Instructors/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name,Experience,CertificateNumber,Phone,DedicatedCarId")] Instructor instructor)
        {
            if (id != instructor.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(instructor);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!InstructorExists(instructor.Id))
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
            return View(instructor);
        }

        // GET: Instructors/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null || _context.instructors == null)
            {
                return NotFound();
            }

            var instructor = await _context.instructors
                .FirstOrDefaultAsync(m => m.Id == id);
            if (instructor == null)
            {
                return NotFound();
            }

            return View(instructor);
        }

        // POST: Instructors/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            if (_context.instructors == null)
            {
                return Problem("Entity set 'DDBLab6Context.Instructor'  is null.");
            }
            var instructor = await _context.instructors.FindAsync(id);
            if (instructor != null)
            {
                _context.instructors.Remove(instructor);
            }
            
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool InstructorExists(int id)
        {
          return (_context.instructors?.Any(e => e.Id == id)).GetValueOrDefault();
        }
    }
}
