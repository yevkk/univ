using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Lens
    {
        public int Id { get; set; }
        public string Manufacturer { get; set; }
        [Required]
        public string Model { get; set; }
        [Required]
        public int Aperture { get; set; }
        [Required]
        public int Min_focal_length{ get; set; }
        public int Max_focal_length { get; set; }
        [Required]
        public int Mounting_id { get; set; }
    }
}
