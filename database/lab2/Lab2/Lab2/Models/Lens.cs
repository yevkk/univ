using System;
using System.ComponentModel.DataAnnotations;

namespace Lab2.Models
{
    public class Lens
    {
        public int Id { get; set; }
        [MaxLength(50)]
        public string Manufacturer { get; set; }
        [MaxLength(50)]
        [Required]
        public string Model { get; set; }
        [Range(0, 100)]
        [Required]
        public int Aperture { get; set; }
        [Range(0,20000)]
        [Required]
        public int Min_focal_length{ get; set; }
        [Range(0, 20000)]
        public int Max_focal_length { get; set; }
        [Required]
        public int Mounting_id { get; set; }
    }
}
