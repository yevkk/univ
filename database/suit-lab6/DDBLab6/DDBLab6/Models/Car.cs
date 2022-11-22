using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace DDBLab6.Models
{
    public class Car
    {
        [Column("id")]
        public int Id { get; set; }

        [Column("manufacturer")]
        public string? Manufacturer { get; set; }

        [Column("model")]
        public string? Model { get; set; }

        [Column("color")]
        public string? Color { get; set; }

        [Column("license_plate")]
        [Display(Name = "License Plate")]
        public string? LicensePlate { get; set; }

        [Column("gearbox")]
        public char Gearbox { get; set; }

        public string Desc => string.Format("{0} {1}, {2}", Manufacturer, Model, LicensePlate);
    }
}
