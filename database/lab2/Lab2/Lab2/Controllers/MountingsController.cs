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
    public class MountingsController : Controller
    {
        private readonly Lab2Context _context;

        public MountingsController(Lab2Context context)
        {
            _context = context;
        }

        // GET: Mountings
        public async Task<IActionResult> Index()
        {
            return View(await _context.Mounting.ToListAsync());
        }


        // GET: Mountings/Details/5
        public async Task<IActionResult> Details(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var mounting = await _context.Mounting
                .FirstOrDefaultAsync(m => m.Id == id);
            if (mounting == null)
            {
                return NotFound();
            }

            return View(mounting);
        }

        // GET: Mountings/Create
        public IActionResult Create()
        {
            return View();
        }

        // POST: Mountings/Create
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Create([Bind("Id,Name")] Mounting mounting)
        {
            if (ModelState.IsValid)
            {
                _context.Add(mounting);
                await _context.SaveChangesAsync();
                return RedirectToAction(nameof(Index));
            }
            return View(mounting);
        }

        // GET: Mountings/Edit/5
        public async Task<IActionResult> Edit(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var mounting = await _context.Mounting.FindAsync(id);
            if (mounting == null)
            {
                return NotFound();
            }
            return View(mounting);
        }

        // POST: Mountings/Edit/5
        // To protect from overposting attacks, enable the specific properties you want to bind to.
        // For more details, see http://go.microsoft.com/fwlink/?LinkId=317598.
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> Edit(int id, [Bind("Id,Name")] Mounting mounting)
        {
            if (id != mounting.Id)
            {
                return NotFound();
            }

            if (ModelState.IsValid)
            {
                try
                {
                    _context.Update(mounting);
                    await _context.SaveChangesAsync();
                }
                catch (DbUpdateConcurrencyException)
                {
                    if (!MountingExists(mounting.Id))
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
            return View(mounting);
        }

        // GET: Mountings/Delete/5
        public async Task<IActionResult> Delete(int? id)
        {
            if (id == null)
            {
                return NotFound();
            }

            var mounting = await _context.Mounting
                .FirstOrDefaultAsync(m => m.Id == id);
            if (mounting == null)
            {
                return NotFound();
            }

            return View(mounting);
        }

        // POST: Mountings/Delete/5
        [HttpPost, ActionName("Delete")]
        [ValidateAntiForgeryToken]
        public async Task<IActionResult> DeleteConfirmed(int id)
        {
            var mounting = await _context.Mounting.FindAsync(id);
            _context.Mounting.Remove(mounting);
            await _context.SaveChangesAsync();
            return RedirectToAction(nameof(Index));
        }

        private bool MountingExists(int id)
        {
            return _context.Mounting.Any(e => e.Id == id);
        }
    }
}
